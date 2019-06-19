/*********************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
(function() {

    'use strict';

    angular.module('bannerBindkeys', []).provider('hotkeys', function() {

        this.includeCheatSheet = true;

        this.templateTitle = '';

        this.template = '';

        this.cheatSheetHotkey = '';

        this.cheatSheetDescription = '';

        this.$get = ['$rootElement', '$rootScope', '$compile', '$window', '$document', function ($rootElement, $rootScope, $compile, $window, $document) {

            Mousetrap.stopCallback = function(event, element) {
                if ((' ' + element.className + ' ').indexOf(' mousetrap ') > -1) {
                    return false;
                }
                return (element.contentEditable && element.contentEditable == 'true');
            };

            function symbolize (combo) {
                var map = {
                    command   : '\u2318',     // ?
                    shift     : '\u21E7',     // ?
                    left      : '\u2190',     // ?
                    right     : '\u2192',     // ?
                    up        : '\u2191',     // ?
                    down      : '\u2193',     // ?
                    'return'  : '\u23CE',     // ?
                    backspace : '\u232B'      // ?
                };
                combo = combo.split('+');

                for (var i = 0; i < combo.length; i++) {
                    // try to resolve command / ctrl based on OS:
                    if (combo[i] === 'mod') {
                        if ($window.navigator && $window.navigator.platform.indexOf('Mac') >=0 ) {
                            combo[i] = 'command';
                        } else {
                            combo[i] = 'ctrl';
                        }
                    }

                    combo[i] = map[combo[i]] || combo[i];
                }

                return combo.join(' + ');
            }

            /**
             * Hotkey object used internally for consistency
             *
             * @param {array}    combo       The keycombo. it's an array to support multiple combos
             * @param {String}   description Description for the keycombo
             * @param {Function} callback    function to execute when keycombo pressed
             * @param {string}   action      the type of event to listen for (for mousetrap)
             * @param {array}    allowIn     an array of tag names to allow this combo in ('INPUT', 'SELECT', and/or 'TEXTAREA')
             * @param {Boolean}  persistent  Whether the hotkey persists navigation events
             */
            function Hotkey (combo, description, callback, action, allowIn, persistent) {
                this.combo = combo instanceof Array ? combo : [combo];
                this.description = description;
                this.callback = callback;
                this.action = action;
                this.allowIn = allowIn;
                this.persistent = persistent;
            }

            /**
             * A new scope used internally for the cheatsheet
             * @type {$rootScope.Scope}
             */
            var scope = $rootScope.$new();

            /**
             * Holds an array of Hotkey objects currently bound
             * @type {Array}
             */
            scope.hotkeys = [];

            /**
             * Contains the state of the help's visibility
             * @type {Boolean}
             */
            scope.helpVisible = false;

            /**
             * Holds the title string for the help menu
             * @type {String}
             */
            scope.title = this.templateTitle;

            /**
             * Expose toggleCheatSheet to hotkeys scope so we can call it using
             * ng-click from the template
             * @type {function}
             */
            scope.toggleCheatSheet = toggleCheatSheet;


            /**
             * Holds references to the different scopes that have bound hotkeys
             * attached.  This is useful to catch when the scopes are `$destroy`d and
             * then automatically unbind the hotkey.
             *
             * @type {Array}
             */
            var boundScopes = [];


            $rootScope.$on('$routeChangeSuccess', function (event, route) {
                purgeHotkeys();

                if (route && route.hotkeys) {
                    angular.forEach(route.hotkeys, function (hotkey) {
                        var callback = hotkey[2];
                        if (typeof(callback) === 'string' || callback instanceof String) {
                            hotkey[2] = [callback, route];
                        }
                        hotkey[5] = false;
                        _add.apply(this, hotkey);
                    });
                }
            });


            // Auto-create a help menu:
            if (this.includeCheatSheet) {
                var document = $document[0];
                var element = $rootElement[0];
                var helpMenu = angular.element(this.template);
                if(this.cheatSheetHotkey !== ""){
                    _add(this.cheatSheetHotkey, this.cheatSheetDescription, toggleCheatSheet);
                }
                ;
                // If $rootElement is document or documentElement, then body must be used
                if (element === document || element === document.documentElement) {
                    element = document.body;
                }

                angular.element(element).append($compile(helpMenu)(scope));
            }


            function purgeHotkeys() {
                var i = scope.hotkeys.length;
                while (i--) {
                    var hotkey = scope.hotkeys[i];
                    if (hotkey && !hotkey.persistent) {
                        _del(hotkey);
                    }
                }
            }

            var previousEsc = false;


            function toggleCheatSheet() {
                scope.helpVisible = !scope.helpVisible;
                if (scope.helpVisible) {
                    previousEsc = _get('esc');
                    _del('esc');
                    _add('esc', previousEsc.description, toggleCheatSheet);
                } else {
                    _del('esc');
                    // restore the previously bound ESC key
                    if (previousEsc !== false) {
                        _add(previousEsc);
                    }
                }
            }

            /**
             * Creates a new Hotkey and creates the Mousetrap binding
             *
             * @param {string}   combo       mousetrap key binding
             * @param {string}   description description for the help menu
             * @param {Function} callback    method to call when key is pressed
             * @param {string}   action      the type of event to listen for (for mousetrap)
             * @param {array}    allowIn     an array of tag names to allow this combo in ('INPUT', 'SELECT', and/or 'TEXTAREA')
             * @param {boolean}  persistent  if true, the binding is preserved upon route changes
             */
            function _add (combo, description, callback, action, allowIn, persistent) {

                var _callback;

                var preventIn = ['INPUT', 'SELECT', 'TEXTAREA'];

                var objType = Object.prototype.toString.call(combo);

                if (objType === '[object Object]') {
                    description = combo.description;
                    callback    = combo.callback;
                    action      = combo.action;
                    persistent  = combo.persistent;
                    allowIn     = combo.allowIn;
                    combo       = combo.combo;
                }

                if (description instanceof Function) {
                    action = callback;
                    callback = description;
                    description = '$$undefined$$';
                } else if (angular.isUndefined(description)) {
                    description = '$$undefined$$';
                }

                if (persistent === undefined) {
                    persistent = true;
                }

                if (typeof callback === 'function') {

                    _callback = callback;

                    if (!(allowIn instanceof Array)) {
                        allowIn = [];
                    }

                    var index;
                    for (var i=0; i < allowIn.length; i++) {
                        allowIn[i] = allowIn[i].toUpperCase();
                        index = preventIn.indexOf(allowIn[i]);
                        if (index !== -1) {
                            preventIn.splice(index, 1);
                        }
                    }

                    callback = function(event) {
                        var shouldExecute = true;
                        var target = event.target || event.srcElement; // srcElement is IE only
                        var nodeName = target.nodeName.toUpperCase();

                        // check if the input has a mousetrap class, and skip checking preventIn if so
                        if ((' ' + target.className + ' ').indexOf(' mousetrap ') > -1) {
                            shouldExecute = true;
                        } else {
                            // don't execute callback if the event was fired from inside an element listed in preventIn
                            for (var i=0; i<preventIn.length; i++) {
                                if (preventIn[i] === nodeName) {
                                    shouldExecute = false;
                                    break;
                                }
                            }
                        }

                        if (shouldExecute) {
                            wrapApply(_callback.apply(this, arguments));
                        }
                    };
                }

                if (typeof(action) === 'string') {
                    Mousetrap.bind(combo, wrapApply(callback), action);
                } else {
                    Mousetrap.bind(combo, wrapApply(callback));
                }

                var hotkey = new Hotkey(combo, description, callback, action, allowIn, persistent);
                scope.hotkeys.push(hotkey);
                return hotkey;
            }

            function _del (hotkey) {
                var combo = (hotkey instanceof Hotkey) ? hotkey.combo : hotkey;

                Mousetrap.unbind(combo);

                if (angular.isArray(combo)) {
                    var retStatus = true;
                    var i = combo.length;
                    while (i--) {
                        retStatus = _del(combo[i]) && retStatus;
                    }
                    return retStatus;
                } else {
                    var index = scope.hotkeys.indexOf(_get(combo));

                    if (index > -1) {
                        // if the combo has other combos bound, don't unbind the whole thing, just the one combo:
                        if (scope.hotkeys[index].combo.length > 1) {
                            scope.hotkeys[index].combo.splice(scope.hotkeys[index].combo.indexOf(combo), 1);
                        } else {
                            scope.hotkeys.splice(index, 1);
                        }
                        return true;
                    }
                }

                return false;

            }

            /**
             * Get a Hotkey object by key binding
             *
             * @param  {[string]} combo  the key the Hotkey is bound to
             * @return {Hotkey}          The Hotkey object
             */
            function _get (combo) {

                var hotkey;

                for (var i = 0; i < scope.hotkeys.length; i++) {
                    hotkey = scope.hotkeys[i];

                    if (hotkey.combo.indexOf(combo) > -1) {
                        return hotkey;
                    }
                }

                return false;
            }

            /**
             * Binds the hotkey to a particular scope.  Useful if the scope is
             * destroyed, we can automatically destroy the hotkey binding.
             *
             * @param  {Object} scope The scope to bind to
             */
            function bindTo (scope) {
                // Only initialize once to allow multiple calls for same scope.
                if (!(scope.$id in boundScopes)) {

                    // Add the scope to the list of bound scopes
                    boundScopes[scope.$id] = [];

                    scope.$on('$destroy', function () {
                        var i = boundScopes[scope.$id].length;
                        while (i--) {
                            _del(boundScopes[scope.$id][i]);
                            delete boundScopes[scope.$id][i];
                        }
                    });
                }
                // return an object with an add function so we can keep track of the
                // hotkeys and their scope that we added via this chaining method
                return {
                    add: function (args) {
                        var hotkey;

                        if (arguments.length > 1) {
                            hotkey = _add.apply(this, arguments);
                        } else {
                            hotkey = _add(args);
                        }

                        boundScopes[scope.$id].push(hotkey);
                        return this;
                    }
                };
            }

            /**
             * All callbacks sent to Mousetrap are wrapped using this function
             * so that we can force a $scope.$apply()
             *
             * @param  {Function} callback [description]
             * @return {[type]}            [description]
             */
            function wrapApply (callback) {
                // return mousetrap a function to call
                return function (event, combo) {

                    if (callback instanceof Array) {
                        var funcString = callback[0];
                        var route = callback[1];
                        callback = function (event) {
                            route.scope.$eval(funcString);
                        };
                    }

                    $rootScope.$apply(function() {
                        callback(event, _get(combo));
                    });
                };
            }


            var publicApi = {
                add                   : _add,
                del                   : _del,
                get                   : _get,
                bindTo                : bindTo,
                template              : this.template,
                toggleCheatSheet      : toggleCheatSheet,
                includeCheatSheet     : this.includeCheatSheet,
                cheatSheetHotkey      : this.cheatSheetHotkey,
                cheatSheetDescription : this.cheatSheetDescription,
                purgeHotkeys          : purgeHotkeys,
                templateTitle         : this.templateTitle
            };

            return publicApi;

        }];
    })

        .directive('hotkey', ['hotkeys', function (hotkeys) {
            return {
                restrict: 'A',
                link: function (scope, el, attrs) {
                    var key, allowIn;

                    angular.forEach(scope.$eval(attrs.hotkey), function (func, hotkey) {
                        // split and trim the hotkeys string into array
                        allowIn = typeof attrs.hotkeyAllowIn === "string" ? attrs.hotkeyAllowIn.split(/[\s,]+/) : [];

                        key = hotkey;

                        hotkeys.add({
                            combo: hotkey,
                            description: attrs.hotkeyDescription,
                            callback: func,
                            action: attrs.hotkeyAction,
                            allowIn: allowIn
                        });
                    });

                    // remove the hotkey if the directive is destroyed:
                    el.bind('$destroy', function() {
                        hotkeys.del(key);
                    });
                }
            };
        }])

        .run(['hotkeys', function(hotkeys) {
        }]);

})();

/*global define:false */
(function(window, document, undefined) {

    /**
     * mapping of special keycodes to their corresponding keys
     *
     * everything in this dictionary cannot use keypress events
     * so it has to be here to map to the correct keycodes for
     * keyup/keydown events
     *
     * @type {Object}
     */
    var _MAP = {
            8: 'backspace',
            9: 'tab',
            13: 'enter',
            16: 'shift',
            17: 'ctrl',
            18: 'alt',
            20: 'capslock',
            27: 'esc',
            32: 'space',
            33: 'pageup',
            34: 'pagedown',
            35: 'end',
            36: 'home',
            37: 'left',
            38: 'up',
            39: 'right',
            40: 'down',
            45: 'ins',
            46: 'del',
            91: 'meta',
            93: 'meta',
            224: 'meta'
        },

        /**
         * mapping for special characters so they can support
         *
         * this dictionary is only used incase you want to bind a
         * keyup or keydown event to one of these keys
         *
         * @type {Object}
         */
        _KEYCODE_MAP = {
            106: '*',
            107: '+',
            109: '-',
            110: '.',
            111 : '/',
            186: ';',
            187: '=',
            188: ',',
            189: '-',
            190: '.',
            191: '/',
            192: '`',
            219: '[',
            220: '\\',
            221: ']',
            222: '\''
        },

        /**
         * this is a mapping of keys that require shift on a US keypad
         * back to the non shift equivelents
         *
         * this is so you can use keyup events with these keys
         *
         * note that this will only work reliably on US keyboards
         *
         * @type {Object}
         */
        _SHIFT_MAP = {
            '~': '`',
            '!': '1',
            '@': '2',
            '#': '3',
            '$': '4',
            '%': '5',
            '^': '6',
            '&': '7',
            '*': '8',
            '(': '9',
            ')': '0',
            '_': '-',
            '+': '=',
            ':': ';',
            '\"': '\'',
            '<': ',',
            '>': '.',
            '?': '/',
            '|': '\\'
        },

        /**
         * this is a list of special strings you can use to map
         * to modifier keys when you specify your keyboard shortcuts
         *
         * @type {Object}
         */
        _SPECIAL_ALIASES = {
            'option': 'alt',
            'command': 'meta',
            'return': 'enter',
            'escape': 'esc',
            'mod': /Mac|iPod|iPhone|iPad/.test(navigator.platform) ? 'meta' : 'ctrl'
        },

        /**
         * variable to store the flipped version of _MAP from above
         * needed to check if we should use keypress or not when no action
         * is specified
         *
         * @type {Object|undefined}
         */
        _REVERSE_MAP,

        /**
         * a list of all the callbacks setup via Mousetrap.bind()
         *
         * @type {Object}
         */
        _callbacks = {},

        /**
         * direct map of string combinations to callbacks used for trigger()
         *
         * @type {Object}
         */
        _directMap = {},

        /**
         * keeps track of what level each sequence is at since multiple
         * sequences can start out with the same sequence
         *
         * @type {Object}
         */
        _sequenceLevels = {},

        /**
         * variable to store the setTimeout call
         *
         * @type {null|number}
         */
        _resetTimer,

        /**
         * temporary state where we will ignore the next keyup
         *
         * @type {boolean|string}
         */
        _ignoreNextKeyup = false,

        /**
         * temporary state where we will ignore the next keypress
         *
         * @type {boolean}
         */
        _ignoreNextKeypress = false,

        /**
         * are we currently inside of a sequence?
         * type of action ("keyup" or "keydown" or "keypress") or false
         *
         * @type {boolean|string}
         */
        _nextExpectedAction = false;

    /**
     * loop through the f keys, f1 to f19 and add them to the map
     * programatically
     */
    for (var i = 1; i < 20; ++i) {
        _MAP[111 + i] = 'f' + i;
    }

    /**
     * loop through to map numbers on the numeric keypad
     */
    for (i = 0; i <= 9; ++i) {
        _MAP[i + 96] = i;
    }

    /**
     * cross browser add event method
     *
     * @param {Element|HTMLDocument} object
     * @param {string} type
     * @param {Function} callback
     * @returns void
     */
    function _addEvent(object, type, callback) {
        if (object.addEventListener) {
            object.addEventListener(type, callback, false);
            return;
        }

        object.attachEvent('on' + type, callback);
    }

    /**
     * takes the event and returns the key character
     *
     * @param {Event} e
     * @return {string}
     */
    function _characterFromEvent(e) {

        // for keypress events we should return the character as is
        if (e.type == 'keypress') {
            var character = String.fromCharCode(e.which);
            if (!e.shiftKey) {
                character = character.toLowerCase();
            }

            return character;
        }

        if (_MAP[e.which]) {
            return _MAP[e.which];
        }

        if (_KEYCODE_MAP[e.which]) {
            return _KEYCODE_MAP[e.which];
        }

        return String.fromCharCode(e.which).toLowerCase();
    }

    /**
     * checks if two arrays are equal
     *
     * @param {Array} modifiers1
     * @param {Array} modifiers2
     * @returns {boolean}
     */
    function _modifiersMatch(modifiers1, modifiers2) {
        return modifiers1.sort().join(',') === modifiers2.sort().join(',');
    }

    /**
     * resets all sequence counters except for the ones passed in
     *
     * @param {Object} doNotReset
     * @returns void
     */
    function _resetSequences(doNotReset) {
        doNotReset = doNotReset || {};

        var activeSequences = false,
            key;

        for (key in _sequenceLevels) {
            if (doNotReset[key]) {
                activeSequences = true;
                continue;
            }
            _sequenceLevels[key] = 0;
        }

        if (!activeSequences) {
            _nextExpectedAction = false;
        }
    }

    /**
     * finds all callbacks that match based on the keycode, modifiers,
     * and action
     *
     * @param {string} character
     * @param {Array} modifiers
     * @param {Event|Object} e
     * @param {string=} sequenceName - name of the sequence we are looking for
     * @param {string=} combination
     * @param {number=} level
     * @returns {Array}
     */
    function _getMatches(character, modifiers, e, sequenceName, combination, level) {
        var i,
            callback,
            matches = [],
            action = e.type;

        // if there are no events related to this keycode
        if (!_callbacks[character]) {
            return [];
        }

        // if a modifier key is coming up on its own we should allow it
        if (action == 'keyup' && _isModifier(character)) {
            modifiers = [character];
        }

        // loop through all callbacks for the key that was pressed
        // and see if any of them match
        for (i = 0; i < _callbacks[character].length; ++i) {
            callback = _callbacks[character][i];

            // if a sequence name is not specified, but this is a sequence at
            // the wrong level then move onto the next match
            if (!sequenceName && callback.seq && _sequenceLevels[callback.seq] != callback.level) {
                continue;
            }

            // if the action we are looking for doesn't match the action we got
            // then we should keep going
            if (action != callback.action) {
                continue;
            }

            if ((action == 'keypress' && !e.metaKey && !e.ctrlKey) || _modifiersMatch(modifiers, callback.modifiers)) {

                var deleteCombo = !sequenceName && callback.combo == combination;
                var deleteSequence = sequenceName && callback.seq == sequenceName && callback.level == level;
                if (deleteCombo || deleteSequence) {
                    _callbacks[character].splice(i, 1);
                }

                matches.push(callback);
            }
        }

        return matches;
    }

    /**
     * takes a key event and figures out what the modifiers are
     *
     * @param {Event} e
     * @returns {Array}
     */
    function _eventModifiers(e) {
        var modifiers = [];

        if (e.shiftKey) {
            modifiers.push('shift');
        }

        if (e.altKey) {
            modifiers.push('alt');
        }

        if (e.ctrlKey) {
            modifiers.push('ctrl');
        }

        if (e.metaKey) {
            modifiers.push('meta');
        }

        return modifiers;
    }

    /**
     * prevents default for this event
     *
     * @param {Event} e
     * @returns void
     */
    function _preventDefault(e) {
        if (e.preventDefault) {
            e.preventDefault();
            return;
        }

        e.returnValue = false;
    }

    /**
     * stops propogation for this event
     *
     * @param {Event} e
     * @returns void
     */
    function _stopPropagation(e) {
        if (e.stopPropagation) {
            e.stopPropagation();
            return;
        }

        e.cancelBubble = true;
    }

    /**
     * actually calls the callback function
     *
     * if your callback function returns false this will use the jquery
     * convention - prevent default and stop propogation on the event
     *
     * @param {Function} callback
     * @param {Event} e
     * @returns void
     */
    function _fireCallback(callback, e, combo, sequence) {

        // if this event should not happen stop here
        if (Mousetrap.stopCallback(e, e.target || e.srcElement, combo, sequence)) {
            return;
        }

        if (callback(e, combo) === false) {
            _preventDefault(e);
            _stopPropagation(e);
        }
    }

    /**
     * handles a character key event
     *
     * @param {string} character
     * @param {Array} modifiers
     * @param {Event} e
     * @returns void
     */
    function _handleKey(character, modifiers, e) {
        var callbacks = _getMatches(character, modifiers, e),
            i,
            doNotReset = {},
            maxLevel = 0,
            processedSequenceCallback = false;

        // Calculate the maxLevel for sequences so we can only execute the longest callback sequence
        for (i = 0; i < callbacks.length; ++i) {
            if (callbacks[i].seq) {
                maxLevel = Math.max(maxLevel, callbacks[i].level);
            }
        }

        // loop through matching callbacks for this key event
        for (i = 0; i < callbacks.length; ++i) {

            if (callbacks[i].seq) {

                if (callbacks[i].level != maxLevel) {
                    continue;
                }

                processedSequenceCallback = true;

                // keep a list of which sequences were matches for later
                doNotReset[callbacks[i].seq] = 1;
                _fireCallback(callbacks[i].callback, e, callbacks[i].combo, callbacks[i].seq);
                continue;
            }

            // if there were no sequence matches but we are still here
            // that means this is a regular match so we should fire that
            if (!processedSequenceCallback) {
                _fireCallback(callbacks[i].callback, e, callbacks[i].combo);
            }
        }

        var ignoreThisKeypress = e.type == 'keypress' && _ignoreNextKeypress;
        if (e.type == _nextExpectedAction && !_isModifier(character) && !ignoreThisKeypress) {
            _resetSequences(doNotReset);
        }

        _ignoreNextKeypress = processedSequenceCallback && e.type == 'keydown';
    }

    /**
     * handles a keydown event
     *
     * @param {Event} e
     * @returns void
     */
    function _handleKeyEvent(e) {

        // normalize e.which for key events
        // @see http://stackoverflow.com/questions/4285627/javascript-keycode-vs-charcode-utter-confusion
        if (typeof e.which !== 'number') {
            e.which = e.keyCode;
        }

        var character = _characterFromEvent(e);

        // no character found then stop
        if (!character) {
            return;
        }

        // need to use === for the character check because the character can be 0
        if (e.type == 'keyup' && _ignoreNextKeyup === character) {
            _ignoreNextKeyup = false;
            return;
        }

        Mousetrap.handleKey(character, _eventModifiers(e), e);
    }

    /**
     * determines if the keycode specified is a modifier key or not
     *
     * @param {string} key
     * @returns {boolean}
     */
    function _isModifier(key) {
        return key == 'shift' || key == 'ctrl' || key == 'alt' || key == 'meta';
    }

    /**
     * called to set a 1 second timeout on the specified sequence
     *
     * this is so after each key press in the sequence you have 1 second
     * to press the next key before you have to start over
     *
     * @returns void
     */
    function _resetSequenceTimer() {
        clearTimeout(_resetTimer);
        _resetTimer = setTimeout(_resetSequences, 1000);
    }

    /**
     * reverses the map lookup so that we can look for specific keys
     * to see what can and can't use keypress
     *
     * @return {Object}
     */
    function _getReverseMap() {
        if (!_REVERSE_MAP) {
            _REVERSE_MAP = {};
            for (var key in _MAP) {

                if (key > 95 && key < 112) {
                    continue;
                }

                if (_MAP.hasOwnProperty(key)) {
                    _REVERSE_MAP[_MAP[key]] = key;
                }
            }
        }
        return _REVERSE_MAP;
    }

    /**
     * picks the best action based on the key combination
     *
     * @param {string} key - character for key
     * @param {Array} modifiers
     * @param {string=} action passed in
     */
    function _pickBestAction(key, modifiers, action) {

        if (!action) {
            action = _getReverseMap()[key] ? 'keydown' : 'keypress';
        }

        // modifier keys don't work as expected with keypress,
        // switch to keydown
        if (action == 'keypress' && modifiers.length) {
            action = 'keydown';
        }

        return action;
    }

    /**
     * binds a key sequence to an event
     *
     * @param {string} combo - combo specified in bind call
     * @param {Array} keys
     * @param {Function} callback
     * @param {string=} action
     * @returns void
     */
    function _bindSequence(combo, keys, callback, action) {

        _sequenceLevels[combo] = 0;

        /**
         * callback to increase the sequence level for this sequence and reset
         * all other sequences that were active
         *
         * @param {string} nextAction
         * @returns {Function}
         */
        function _increaseSequence(nextAction) {
            return function() {
                _nextExpectedAction = nextAction;
                ++_sequenceLevels[combo];
                _resetSequenceTimer();
            };
        }

        /**
         * wraps the specified callback inside of another function in order
         * to reset all sequence counters as soon as this sequence is done
         *
         * @param {Event} e
         * @returns void
         */
        function _callbackAndReset(e) {
            _fireCallback(callback, e, combo);

            if (action !== 'keyup') {
                _ignoreNextKeyup = _characterFromEvent(e);
            }

            setTimeout(_resetSequences, 10);
        }

        for (var i = 0; i < keys.length; ++i) {
            var isFinal = i + 1 === keys.length;
            var wrappedCallback = isFinal ? _callbackAndReset : _increaseSequence(action || _getKeyInfo(keys[i + 1]).action);
            _bindSingle(keys[i], wrappedCallback, action, combo, i);
        }
    }

    /**
     * Converts from a string key combination to an array
     *
     * @param  {string} combination like "command+shift+l"
     * @return {Array}
     */
    function _keysFromString(combination) {
        if (combination === '+') {
            return ['+'];
        }

        return combination.split('+');
    }

    /**
     * Gets info for a specific key combination
     *
     * @param  {string} combination key combination ("command+s" or "a" or "*")
     * @param  {string=} action
     * @returns {Object}
     */
    function _getKeyInfo(combination, action) {
        var keys,
            key,
            i,
            modifiers = [];

        // take the keys from this pattern and figure out what the actual
        // pattern is all about
        keys = _keysFromString(combination);

        for (i = 0; i < keys.length; ++i) {
            key = keys[i];

            // normalize key names
            if (_SPECIAL_ALIASES[key]) {
                key = _SPECIAL_ALIASES[key];
            }

            if (action && action != 'keypress' && _SHIFT_MAP[key]) {
                key = _SHIFT_MAP[key];
                modifiers.push('shift');
            }

            if (_isModifier(key)) {
                modifiers.push(key);
            }
        }

        action = _pickBestAction(key, modifiers, action);

        return {
            key: key,
            modifiers: modifiers,
            action: action
        };
    }

    /**
     * binds a single keyboard combination
     *
     * @param {string} combination
     * @param {Function} callback
     * @param {string=} action
     * @param {string=} sequenceName - name of sequence if part of sequence
     * @param {number=} level - what part of the sequence the command is
     * @returns void
     */
    function _bindSingle(combination, callback, action, sequenceName, level) {

        _directMap[combination + ':' + action] = callback;

        combination = combination.replace(/\s+/g, ' ');

        var sequence = combination.split(' '),
            info;

        if (sequence.length > 1) {
            _bindSequence(combination, sequence, callback, action);
            return;
        }

        info = _getKeyInfo(combination, action);

        _callbacks[info.key] = _callbacks[info.key] || [];

        // remove an existing match if there is one
        _getMatches(info.key, info.modifiers, {type: info.action}, sequenceName, combination, level);

        _callbacks[info.key][sequenceName ? 'unshift' : 'push']({
            callback: callback,
            modifiers: info.modifiers,
            action: info.action,
            seq: sequenceName,
            level: level,
            combo: combination
        });
    }

    /**
     * binds multiple combinations to the same callback
     *
     * @param {Array} combinations
     * @param {Function} callback
     * @param {string|undefined} action
     * @returns void
     */
    function _bindMultiple(combinations, callback, action) {
        for (var i = 0; i < combinations.length; ++i) {
            _bindSingle(combinations[i], callback, action);
        }
    }

    // start!
    _addEvent(document, 'keypress', _handleKeyEvent);
    _addEvent(document, 'keydown', _handleKeyEvent);
    _addEvent(document, 'keyup', _handleKeyEvent);

    var Mousetrap = {

        /**
         * binds an event to mousetrap
         *
         * can be a single key, a combination of keys separated with +,
         * an array of keys, or a sequence of keys separated by spaces
         *
         * be sure to list the modifier keys first to make sure that the
         * correct key ends up getting bound (the last key in the pattern)
         *
         * @param {string|Array} keys
         * @param {Function} callback
         * @param {string=} action - 'keypress', 'keydown', or 'keyup'
         * @returns void
         */
        bind: function(keys, callback, action) {
            keys = keys instanceof Array ? keys : [keys];
            _bindMultiple(keys, callback, action);
            return this;
        },

        /**
         * unbinds an event to mousetrap
         *
         * the unbinding sets the callback function of the specified key combo
         * to an empty function and deletes the corresponding key in the
         * _directMap dict.
         *
         * TODO: actually remove this from the _callbacks dictionary instead
         * of binding an empty function
         *
         * the keycombo+action has to be exactly the same as
         * it was defined in the bind method
         *
         * @param {string|Array} keys
         * @param {string} action
         * @returns void
         */
        unbind: function(keys, action) {
            return Mousetrap.bind(keys, function() {}, action);
        },

        /**
         * triggers an event that has already been bound
         *
         * @param {string} keys
         * @param {string=} action
         * @returns void
         */
        trigger: function(keys, action) {
            if (_directMap[keys + ':' + action]) {
                _directMap[keys + ':' + action]({}, keys);
            }
            return this;
        },

        /**
         * resets the library back to its initial state.  this is useful
         * if you want to clear out the current keyboard shortcuts and bind
         * new ones - for example if you switch to another page
         *
         * @returns void
         */
        reset: function() {
            _callbacks = {};
            _directMap = {};
            return this;
        },

        /**
         * should we stop this event before firing off callbacks
         *
         * @param {Event} e
         * @param {Element} element
         * @return {boolean}
         */
        stopCallback: function(e, element) {

            // if the element has the class "mousetrap" then no need to stop
            if ((' ' + element.className + ' ').indexOf(' mousetrap ') > -1) {
                return false;
            }

            // stop for input, select, and textarea
            return element.tagName == 'INPUT' || element.tagName == 'SELECT' || element.tagName == 'TEXTAREA' || element.isContentEditable;
        },

        /**
         * exposes _handleKey publicly so it can be overwritten by extensions
         */
        handleKey: _handleKey
    };

    // expose mousetrap to the global object
    window.Mousetrap = Mousetrap;

    // expose mousetrap as an AMD module
    if (typeof define === 'function' && define.amd) {
        define(Mousetrap);
    }
}) (window, document);