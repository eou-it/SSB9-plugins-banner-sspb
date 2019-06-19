( function ( $, _ ) {
    $.widget("ui.pillbox", {
        options: {
            sunday: { enabled: true, highlight:false },
            monday: { enabled: true,  highlight:false },
            tuesday: { enabled: true,  highlight:false },
            wednesday: { enabled: true,  highlight:false },
            thursday: { enabled: true,  highlight:false },
            friday: {  enabled: true,  highlight:false },
            saturday: { enabled: true, highlight:false },
            styles: {
                container: "ui-pillbox",
                highlight: "ui-state-highlight",
                disabled:  "ui-state-disabled",
                normal:    "ui-state-default",
                summary:   "ui-pillbox-summary"
            },
            editable: false,
            items: [
                // { name: String, abbreviation: String, enabled: Boolean (Default true), highlight: Boolean (Default false) }
            ]
        },
        elements: {
            div:  "<div></div>",
            span: "<span></span>",
            ul:   "<ul></ul>",
            li:   "<li></li>"
        },
        updateItemState: function (el, highlight) {
            var $el = $(el),
                name = $el.attr( "data-name" ),
                flag = _.isUndefined(highlight) ? !$(el).is('.' + this.options.styles.highlight) : highlight;
                text = flag ?
                    $.i18n.prop( "ui.pillbox.on", [name] ) :
                    $.i18n.prop( "ui.pillbox.off", [name] );

            $el.toggleClass( this.options.styles.highlight, flag ).attr('aria-checked', flag ? 'true' : 'false');
            //editable:$el.screenReaderLabel(text, 'assertive');
        },
        updateTooltipText: function () {
            var items = _.map( $( this.element ).find ( "." + this.options.styles.highlight ), function ( it ) {
                return $( it ).attr( "data-name" );
            });

            var tooltipText = items.length > 0 ? items.join( $.i18n.prop( "ui.pillbox.tooltip.separator" ) ) : $.i18n.prop( "default.none" );

            $( this.element ).attr( "title", $.i18n.prop( "ui.pillbox.tooltip.label", [ tooltipText ] ) );

            //editable:$( this.element ).screenReaderLabel( tooltipText, 'assertive' );
            $( "." + this.options.styles.summary, this.element ).screenReaderOnly().text( tooltipText );
        },
        _initializeLabels: function() {
            this.options.sunday.name = $.i18n.prop( "pillbox.sunday" );
            this.options.sunday.abbreviation= $.i18n.prop( "pillbox.sunday.abbreviation");
            this.options.monday.name = $.i18n.prop( "pillbox.monday" );
            this.options.monday.abbreviation= $.i18n.prop( "pillbox.monday.abbreviation");
            this.options.tuesday.name = $.i18n.prop( "pillbox.tuesday" );
            this.options.tuesday.abbreviation= $.i18n.prop( "pillbox.tuesday.abbreviation");
            this.options.wednesday.name = $.i18n.prop( "pillbox.wednesday" );
            this.options.wednesday.abbreviation= $.i18n.prop( "pillbox.wednesday.abbreviation");
            this.options.thursday.name = $.i18n.prop( "pillbox.thursday" );
            this.options.thursday.abbreviation= $.i18n.prop( "pillbox.thursday.abbreviation");
            this.options.friday.name = $.i18n.prop( "pillbox.friday" );
            this.options.friday.abbreviation= $.i18n.prop( "pillbox.friday.abbreviation");
            this.options.saturday.name = $.i18n.prop( "pillbox.saturday" );
            this.options.saturday.abbreviation= $.i18n.prop( "pillbox.saturday.abbreviation");
        },
        _reOrderPillBoxElements: function() {
            var firstDayOfWeek=$.i18n.prop( "default.firstDayOfTheWeek" );
            var unOrderedElements = [this.options.sunday, this.options.monday, this.options.tuesday, this.options.wednesday, this.options.thursday, this.options.friday, this.options.saturday];
            var orderedElements = unOrderedElements.slice(firstDayOfWeek,unOrderedElements.length);
            orderedElements = orderedElements.concat(unOrderedElements.slice(0,firstDayOfWeek));
            return orderedElements;
        },
        _create: function() {
            var self    = this,
                summary = $( this.elements.div ).addClass(this.options.styles.summary),
                list    = $( this.elements.ul ),
                el      = $( this.element );

            el.addClass( this.options.styles.container ).attr( "tabindex", 0 ).attr('role','group');
            this._initializeLabels();
            this.options.items = this._reOrderPillBoxElements();
            var toggleSelected = function ( el ) {
                if ( !$( el ).hasClass( self.options.styles.disabled ) ) {
                    self.updateItemState( el );
                    self.updateTooltipText();
                }
            };

            var onClick = function ( e ) {
                var el = $( e.target ).is( "li" ) ? $( e.target ) : $( e.target ).closest( "li" );

                toggleSelected( el );

                e.preventDefault();
                e.stopPropagation();
            };

            var onKeypress = function ( e ) {
                var key = e.keyCode ? e.keyCode : e.which;

                if ( key == 32 || key == 13 ) {
                    var el = $( e.target ).is( "li" ) ? $( e.target ) : $( e.target ).closest( "li" );

                    toggleSelected( el );

                    e.preventDefault();
                    e.stopPropagation();
                }
            };

            _.each( this.options.items, function ( it ) {
                var item = $( self.elements.li ).addClass( self.options.styles.normal );
                var tabindex = 0;

                if ( !it.enabled ) {
                    item.addClass( self.options.styles.disabled ).attr({
                        'aria-disabled': 'true'
                    });
                    tabindex=-1;
                } else if ( self.options.editable ) {
                    item.attr( "tabindex", 0 ).attr('role','checkbox')
                        .click( onClick )
                        .keypress( onKeypress );
                }

                item.attr( "data-name", it.name );
                item.attr( "data-abbreviation", it.abbreviation );

                item.append( $( self.elements.div ).text( it.abbreviation ));

                self.updateItemState( item, it.highlight );

                list.append( item );
            });

            if ( !self.options.editable ) {
                list.screenReaderHide(true);
            }
            el.append( summary );
            el.append( list );

            this.updateTooltipText();

            // if ( _.isFunction( self.element.tooltip ) )
            //     el.tooltip();
        }
    });

} ).call( this, $, _ );
