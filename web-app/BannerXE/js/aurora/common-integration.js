/*
 * ******************************************************************************
 *  © 2011 SunGard Higher Education.  All Rights Reserved.
 *
 *  CONFIDENTIAL BUSINESS INFORMATION
 *
 *  THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 *  AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 *  NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 *  WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 *  ******************************************************************************
 */

/**
 * @class The primary integration component for a managed application. Controls
 * message processing between managed applications and the Common UI Platform.
 *
 * @author jmiller
 */
var Messenger = {
    /**
     * @private
     * The communication type to use for messaging managed application.
     * @default "hash"
     */
    commtype: "hash",
    /**
     * @private
     * The source URL for the Common UI Platform.
     * @default "about:blank"
     */
    channel: "about:blank",
    /**
     * @private
     * The default proxy page used for sending 'hash' based messages to the Common UI Platform.
     */
    channelIframeSource: "/upcomm.html",
    /**
     * The types of communication channels supported.
     * @default "hash" (URL Fragment Identifier), "xdm" (HTML5 Cross Document Messaging).
     */
    supportedChannels: ["hash", "xdm"],
    /**
     * @private
     * Indicates if the browser supports the HTML5 Cross Document Messaging functionality.
     * @default false
     */
    supportsXDM: false,
    /**
     * @private
     * The default HTML5 XDM origin value to use when sending messaging.
     * @default "*"
     */
    origin: "*",
    /**
     * @private
     * A callback function invoked on message receipt. Allows custom message handling by
     * a managed application.
     */
    messageHandler: null,
    /**
     * Initialization method.

     * @param {Function} callback The message handler function to be invoked when
     *                            messages are recieved. (optional)
     */
    initialize: function(callback) {
		var xmldoc = null;
		
        var msgr = this;

        if (callback) {
            this.messageHandler = callback;
        }

        if (window.postMessage) {
            this.commtype = "xdm";
            this.supportsXDM = true;

            // establish XDM listeners, regardless of the communication channel this child uses.
            if (window.addEventListener) {
                window.addEventListener('message', this.handleCrossDomainMessage, false);
            } else {
                window.attachEvent('onmessage', this.handleCrossDomainMessage, false);
            }
        } else {
            this.commtype = "hash";
        }
    },
    /**
     * @private
     *
     * Sets the reference for the parent's URL. This function is called automatically
     * during by initialize(). It will also establish the channel iframe if using 'hash' based
     * communication.
     *
     * @param {String} parentURL The host parent's URL. (optional)
     */
    setCommunicationChannel: function(channel) {
        this.channel = channel + this.channelIframeSource;

        if (this.commtype == "hash") {
            var channelIFrame = document.createElement("iframe");
            channelIFrame.setAttribute("id",    "up_comm");
            channelIFrame.setAttribute("name",  "up_comm");
            channelIFrame.setAttribute("src",   "about:blank");
            channelIFrame.style.display = "none";

            document.body.appendChild(channelIFrame);
        }
    },
    /**
     * @private
     *
     * Checks the URL fragment identifier (hash) for messages.
     */
    checkFragmentIdentifier: function() {
        var frag = window.location.hash;

        if (frag
         && frag.substr(1) != "none") {
            window.location.hash = "none";
            return this.getXMLDocumentFromFragment(frag);
        } else {
            return null;
        }
    },
    /**
     * @private
     *
     * Polls the URL fragment identifier (hash) for changes.
     */
    pollFragmentIdentifier: function() {
        var frag = Messenger.checkFragmentIdentifier();

        if (frag) {
            if (this.messageHandler) {
                this.messageHandler(frag);
            } else {
                ErrorManager.show('pollFragmentIdentifier: no messageHandler established!');
            }
        }
    },
    /**
     * @private
     *
     * Converts a message in the fragment identifier into a processable XMLDocument Object.
     * Present encoding implementation is a base64 encoded string of serialized XML.
     *
     * @param {String} fragment The fragment identifier to convert.
     */
    getXMLDocumentFromFragment: function(fragment) {
        return this.string2xml(this.decode(fragment));
    },
    handleCrossDomainMessage: function(xdmEvent) {
        if (Messenger.messageHandler) {
            Messenger.messageHandler(Messenger.string2xml(xdmEvent.data));
        } else {
            ErrorManager.show('handleCrossDomainMessage: no messageHandler established!');
        }
    },
    /**
     * Places messages on the proper communication channel.
     *
     * @param {String} message The XML message to send.
     */
    send: function(message) {
        message = this.encode(message);

        switch (this.commtype) {
            case "hash":
                try {
                    document.getElementById('up_comm').src = this.channel + "#" + message;
                } catch (e) {
                    ErrorManager.show("An exception occurred in the script. Error name: " + e.name + ". Error message: " + e.message)
                }
                break;
            case "xdm":
                parent.postMessage(message, this.origin);
                break;
            default:
                ErrorManager.show("No communication method has been defined.");
                break;
        }
    },
	
	/**
     * Supports passing of callback handlers for responses recieved
     * Places messages on the proper communication channel.
     *
     * @param {String} message The XML message to send.
     */
    load: function(url, callback) {

		EventDispatcher.addEventListener(url, callback);
		
		message = createServiceCallMessage(url);
		
        Messenger.send(message);
    },
	
    /**
     * Converts a XMLDocument object to a string.
     *
     * @param {Object} xmlObject
     */
    xml2string: function(xmlObject) {
        var browserName = navigator.appName;
        var xmlstr;

        if (browserName == 'Microsoft Internet Explorer') {
            xmlstr = xmlObject.xml;
        } else {
            xmlstr = (new XMLSerializer()).serializeToString(xmlObject);
        }

        return xmlstr;
    },
    /**
     * Converts a string to a XMLDocument Object.
     *
     * @param {string} xmlString
     */
    string2xml: function(xmlString) {
        var browserName = navigator.appName;
        var xmldoc;

        if (browserName == 'Microsoft Internet Explorer') {
            xmldoc = new ActiveXObject('Microsoft.XMLDOM');
            xmldoc.async = 'false'
            xmldoc.loadXML(xmlString);
        } else {
            xmldoc = (new DOMParser()).parseFromString(xmlString, 'text/xml');
        }
        return xmldoc;
    },
    /**
     * Encodes the supplied message.
     * Present encoding implementation is base64.
     *
     * @param {String} message The message to decode.
     */
    encode: function(input) {
        return Encoder.encode(input);
    },
    /**
     * Decodes the supplied fragment identifier.
     * Present encoding implementation is base64.
     *
     * @param {String} fragment The fragment identifier to decode.
     */
    decode: function(input) {
        if (input.substring(0, 1) == "#") {
            input = input.substr(1);
        }
        return Encoder.decode(input);
    }
};

function wrapMessage(message) {
    return "<?xml version='1.0' encoding='utf-8'?><UDCMessage>" + message + "</UDCMessage>";
}

function createStyleMessage() {
    var message = "<styles>";

    for (var x in Theme.custom.colors) {
         message += "<style>"
                  + "<name>" + x + "</name>"
                  + "<value>" + Theme.custom.colors[x] + "</value>"
                  + "</style>";
    }

    message += "</styles>";

    return message;
}

function createRequestMessage(message) {
    return wrapMessage("<request>" + message + "</request>");
}

function createHostMessage(message) {
    return wrapMessage("<host>" + message + "</host>");
}

function createContextMessage(context) {
    var message = "";

    for (var x in context) {
        message += "<" + x + ">" + context[x] + "</" + x + ">";
    }

    message = "<context>" + message + "</context>";

    message += createStyleMessage();

    return wrapMessage(message);
}

function createNameValuePairMessage(values) {
    var message = "";

    for (x in values) {
        message += "<" + x + ">" + values[x] + "</" + x + ">";
    }

    return wrapMessage(message);
}

function broadcastHelpMessageByURL(url) {
    Messenger.send(createHelpMessageByURL(url));
}

function createHelpMessageByURL(url) {
    return wrapMessage("<help><url>" + url + "</url></help>");
}

function createNavigationMessageByLocation(locationEntry) {
    return wrapMessage("<navigate><location>" + locationEntry + "</location></navigate>");
}

function createApplicationPageNavigationMessage(pageName) {
    return wrapMessage("<navigate><page>" + pageName + "</page></navigate>");
}

function createStatusMessage(message) {
    return wrapMessage("<status><message>" + message + "</message></status>");
}

function createErrorMessage(errors) {
    var out = "";

    if (typeof(errors) === "string") {
        out += "<error><message>" + errors + "</message></error>";
    } else if (errors instanceof Array) {
        for (var x = 0; x < errors.length; x++) {
            if (typeof(errors[x]) === "string") {
                out += "<error><message>" + errors[x] + "</message></error>";
            } else {
                out += "<error>";

                if (errors[x].code) {
                    out += "<code>" + errors[x].code + "</code>";
                }
                if (errors[x].message) {
                    out += "<message>" + errors[x].message + "</message>";
                }

                out += "</error>";
            }
        }
    }

    return wrapMessage("<errors>" + out + "</errors>");
}

function createConfirmationMessage(title, message, responses) {
    var out = "<title>" + title + "</title>";
    out += "<message>" + message + "</message>";
    out += "<responses>";

    for (var x = 0; x < responses.length; x++) {
        out += "<response label=\"" + responses[x].label + "\" responseValue=\"" + responses[x].response + "\"/>";
    }

    out += "</responses>";

    return wrapMessage("<confirmation>" + out + "</confirmation>");
}

function createConfirmationResponseMessage(response) {
    return wrapMessage("<confirmation><response>" + response + "</response></confirmation>");
}

function createServiceCallMessage(url) {
    return wrapMessage("<service><url>" + url + "</url></service>");
}

function createServiceResponseMessage(response, url) {
    return wrapMessage("<service><response>" + response + "</response><url>" + url + "</url></service>");
}

/**
 * @class Encapsulates and implements the message encoding methodology.
 * @author jmiller
 */
var Encoder = {
    //properties
    /**
     * @private
     */
    _keyString: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
    // methods
    /**
     * Encodes the supplied input.
     * @param {string} input The string to encode.
     */
    encode: function(input) {
        var output = "";
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4 = "";
        var i = 0;

        do {
            chr1 = input.charCodeAt(i++);
            chr2 = input.charCodeAt(i++);
            chr3 = input.charCodeAt(i++);

            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;

            if (isNaN(chr2)) {
                enc3 = enc4 = 64;
            } else if (isNaN(chr3)) {
                enc4 = 64;
            }

            output = output
                   + this._keyString.charAt(enc1)
                   + this._keyString.charAt(enc2)
                   + this._keyString.charAt(enc3)
                   + this._keyString.charAt(enc4);

            chr1 = chr2 = chr3 = "";
            enc1 = enc2 = enc3 = enc4 = "";
        } while (i < input.length);

        return output;
    },
    /**
     * Decodes the supplied input.
     * @param {string} input The encoded string to decode.
     */
    decode: function(input) {
        var output = "";
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4 = "";
        var i = 0;

        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

        do {
            enc1 = this._keyString.indexOf(input.charAt(i++));
            enc2 = this._keyString.indexOf(input.charAt(i++));
            enc3 = this._keyString.indexOf(input.charAt(i++));
            enc4 = this._keyString.indexOf(input.charAt(i++));

            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;

            output = output + String.fromCharCode(chr1);

            if (enc3 != 64) {
                output = output + String.fromCharCode(chr2);
            }
            if (enc4 != 64) {
                output = output + String.fromCharCode(chr3);
            }

            chr1 = chr2 = chr3 = "";
            enc1 = enc2 = enc3 = enc4 = "";
        } while (i < input.length);

        return output;
    }
};

/**
 * @class Controls browser cookie access.
 * @author jmiller
 */
var CookieManager = {
    /**
     * @private
     * Domain name to define the cookie for. TODO: external config somehow
     */
    DOMAIN: CookieConstants.domain,
    /**
     * Cookie name for the default home for the user.
     */
    AURORA_HOME_COOKIE: "sghe_aurora_home",
    /**
     * Cookie name for the default home for the user.
     */
    AURORA_COOKIE_MARKER: "sghe_aurora_",
    /**
     * Creates a new cookie or modifies an existing cookie.
     * @param {string} name The name for the cookie.
     * @param {string} value The value for the cookie.
     * @param {string} domain The domain the cookie is applicable to.
     */
    set: function(name, value, domain, minutes) {
        var ttl = 7200;

        var name = CookieManager.AURORA_COOKIE_MARKER + CommonContext.user + "_" + name;

        if (minutes) {
            ttl = minutes;
        }

        var date = new Date();
        date.setTime(date.getTime()+(ttl*60*1000));

        var expires = "expires=" + date.toGMTString();

        document.cookie = name + "=" + value + "; " + expires + "; path=/";
    },
    /**
     * Returns the specified cookie, if it exists.
     * @param {string} name The name of the cookie to retrieve.
     */
    get: function(name) {
        var name = CookieManager.AURORA_COOKIE_MARKER + CommonContext.user + "_" + name + "=";
        var cookies = document.cookie.split(';');
        for (var i=0; i < cookies.length; i++) {
            var c = ' ' + cookies[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1, c.length);

                if (c.indexOf(name) == 0) {
                    return c.substring(name.length, c.length);
                }
            }
        }
        return null;
    },
    /**
     * Removes a cookie.
     * @param {string} name The name of the cookie to remove.
     */
    remove: function(name, domain) {
		var name = CookieManager.AURORA_COOKIE_MARKER + CommonContext.user + "_" + name;
        document.cookie = name + "=; expires=-1; path=/";
    }
};

/**
 * @class Manages the presentation of error messages to the user.
 *
 * @author jmiller
 */
var ErrorManager = {
    show: function (message) {
        switch(typeof(message)) {
            case 'string':
                MessageProcessor.processMessage(Messenger.encode(createErrorMessage(message)));
                break;
            case 'array':
                MessageProcessor.processMessage(Messenger.encode(createErrorMessage("got an array of error messages")));
                break;
            default:
                break;
        }
    }
};

function EventListener(type, listener) {
    this.type = type;
    this.listener = listener;
}

EventListener.prototype.getType = function() {
    return this.type;
}

EventListener.prototype.getListener = function() {
    return this.listener;
}

/**
 * @class Handles dispatched events and executes the cooresponding callback methods that are defined.
 * @author jmiller
 */
var EventDispatcher = {
    /**
     * @private
     *
     * The bound event listeners.
     */
    listeners: [],
    /**
     * Adds an event listener.
     *
     * @param {String} type The type of the event.
     * @param {Function} listener The listener to invoke when the event is dispatched.
     */
    addEventListener: function(type, listener) {
        if (!(type instanceof EventListener)) {
            type = new EventListener(type, listener);
        }

        if (!EventDispatcher.hasEventListener(type)) {
            EventDispatcher.listeners.push(type);
        }
    },
    /**
     * Dispatches the specified event.
     *
     * @param {String} type The type of the event.
     */
    dispatchEvent: function(type, data) {
        for (var x = 0; x < EventDispatcher.listeners.length; x++) {
            if (EventDispatcher.listeners[x].getType() == type) {
                EventDispatcher.listeners[x].getListener()(data);
            }
        }
    },
    /**
     * Determines if an event listener is defined for the specified event type.
     *
     * @param {String} type The type of the event.
     */
    hasEventListener: function(type) {
        if (type instanceof EventListener) {
            for (var x = 0; x < EventDispatcher.listeners.length; x++) {
                if (EventDispatcher.listeners[x].getType() == type.getType()
                 && EventDispatcher.listeners[x].getListener() == type.getListener()) {
                    return true;
                }
            }
        } else {
            for (var x = 0; x < EventDispatcher.listeners.length; x++) {
                if (EventDispatcher.listeners[x].getType() == type) {
                    return true;
                }
            }
        }

        return false;
    },
    /**
     * Removes an event listener.
     *
     * @param {String} type The type of the event.
     * @param {Function} listener The listener to invoke when the event is dispatched.
     */
    removeEventListener: function(type, listener) {
        for (var x = 0; x < EventDispatcher.listeners.length; x++) {
            if (EventDispatcher.listeners[x].getType() == type) {
                EventDispatcher.listeners.splice(x, 1);
                return true;
            }
        }
        return false;
    }
}

/**
 * @class Helper class to simplify XML parsing.
 * @author jmiller
 */
var XMLHelper = {
    /**
     * Cross-browser compatible access to the data in a specified XML Element.
     * @param {XMLDocument} xmldoc The XML document (or document fragment) to parse.
     * @param {string} name The name of the element to retrieve the data from.
     */
    getElementData: function(xmldoc, name) {
        var val = null;

        if (xmldoc.getElementsByTagName(name).length > 0) {
            if (xmldoc.getElementsByTagName(name)[0].textContent) {
                val = xmldoc.getElementsByTagName(name)[0].textContent;
            } else {
                val = xmldoc.getElementsByTagName(name)[0].text;
            }
        }
        return val;
    },
    /**
     * Returns a single child element using its index in the supplied XMLDocument.
     *
     * @param {XMLDocument} xmldoc
     * @param {String} name
     * @param {Number} index
     */
    getChildByIndex: function(xmldoc, name, index) {
        var children = XMLHelper.getChildren(xmldoc, name);

        if (children
         && children[index]) {
            return children[index];
        }
        return null;
    },
    /**
     * Returns all child elements matching the specified name.
     *
     * @param {XMLDocument} xmldoc
     * @param {String} name
     */
    getChildren: function(xmldoc, name) {
        if (xmldoc.getElementsByTagName(name).length > 0) {
            return xmldoc.getElementsByTagName(name);
        }
        return null;
    },
    /**
     * Returns the first child element matching the specified name.
     *
     * @param {XMLDocument} xmldoc
     * @param {String} name
     */
    getFirstChild: function(xmldoc, name) {
        return XMLHelper.getChildByIndex(xmldoc, name, 0);
    }
};

/**
 * @author jmiller
 */
var StyleManager = {
    getRule: function(selector) {
        var sheets = document.styleSheets;

        for (var x = 0; x < sheets.length; x++) {
            var sheet = sheets.item(x);

            var rules = null;

            if (sheet.cssRules) {
                rules = sheet.cssRules;
            } else {
                rules = sheet.rules;
            }

            for (var y = 0; y < rules.length; y++) {
                if (rules.item(y).selectorText == selector) {
                    return rules.item(y);
                }
            }
        }
        return null;
    },
    getStyle: function(selector, property) {
        var rule = StyleManager.getRule(selector);

        if (rule
         && rule.style
         && rule.style[property]) {
            return rule.style[property];
        }
        return null;
    },
    setStyle: function(selector, property, value) {
        var rule = StyleManager.getRule(selector);

        if (!rule) {
            return;
        }

        if (rule.style) {
            rule.style[property] = value;
        }
    }
};
