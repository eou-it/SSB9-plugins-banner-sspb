/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
 /*currency-directive, decimal directive and percentage-directive does the number formatting through $filter.
 $filter uses the locale specific angular locale files for formatting the number. This file loads the angular locale
 logics as per the browser locale that would be used by $filter. Also, this would do a fall back logic.
 say if we selectthe locale which we don't support fr-fr and it has to fallback to fr.
 */
var locale = $('meta[name=userLocale]').attr("content");
 locale = locale.replace('_','-');
 locale = locale.toLowerCase();
 if(!(locale=="en-us"|| locale=="en-au"|| locale=="en-gb"|| locale=="en-ie"|| locale=="en-in"|| locale=="en" || locale=="fr" || locale=="fr-ca" || locale=="pt" || locale=="es" || locale=="es-mx" || locale=="es-co" || locale=="es-pr" || locale=="es-cl" || locale=="es-cr" || locale=="es-do" || locale=="es-ec" || locale=="es-pe" || locale=="es-ve" || locale=="es-gt" || locale=="es-ar" || locale=="es-pa")){
    var countryLocale=locale.split("-");
    if(countryLocale[0]=="ar"){
        locale="en-us";
    } else
        locale=countryLocale[0];
 }
switch(locale)  {
    case "en-us" :
        angular.module("ngLocale", [], ["$provide", function($provide) {
            var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
            function getDecimals(n) {
                n = n + '';
                var i = n.indexOf('.');
                return (i == -1) ? 0 : n.length - i - 1;
            }

            function getVF(n, opt_precision) {
                var v = opt_precision;

                if (undefined === v) {
                    v = Math.min(getDecimals(n), 3);
                }

                var base = Math.pow(10, v);
                var f = ((n * base) | 0) % base;
                return {v: v, f: f};
            }

            $provide.value("$locale", {
                "DATETIME_FORMATS": {
                    "AMPMS": [
                        "AM",
                        "PM"
                    ],
                    "DAY": [
                        "Sunday",
                        "Monday",
                        "Tuesday",
                        "Wednesday",
                        "Thursday",
                        "Friday",
                        "Saturday"
                    ],
                    "ERANAMES": [
                        "Before Christ",
                        "Anno Domini"
                    ],
                    "ERAS": [
                        "BC",
                        "AD"
                    ],
                    "MONTH": [
                        "January",
                        "February",
                        "March",
                        "April",
                        "May",
                        "June",
                        "July",
                        "August",
                        "September",
                        "October",
                        "November",
                        "December"
                    ],
                    "SHORTDAY": [
                        "Sun",
                        "Mon",
                        "Tue",
                        "Wed",
                        "Thu",
                        "Fri",
                        "Sat"
                    ],
                    "SHORTMONTH": [
                        "Jan",
                        "Feb",
                        "Mar",
                        "Apr",
                        "May",
                        "Jun",
                        "Jul",
                        "Aug",
                        "Sep",
                        "Oct",
                        "Nov",
                        "Dec"
                    ],
                    "fullDate": "EEEE, MMMM d, y",
                    "longDate": "MMMM d, y",
                    "medium": "MMM d, y h:mm:ss a",
                    "mediumDate": "MMM d, y",
                    "mediumTime": "h:mm:ss a",
                    "short": "M/d/yy h:mm a",
                    "shortDate": "M/d/yy",
                    "shortTime": "h:mm a"
                },
                "NUMBER_FORMATS": {
                    "CURRENCY_SYM": "$",
                    "DECIMAL_SEP": ".",
                    "GROUP_SEP": ",",
                    "PATTERNS": [
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 3,
                            "minFrac": 0,
                            "minInt": 1,
                            "negPre": "-",
                            "negSuf": "",
                            "posPre": "",
                            "posSuf": ""
                        },
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 2,
                            "minFrac": 2,
                            "minInt": 1,
                            "negPre": "\u00a4-",
                            "negSuf": "",
                            "posPre": "\u00a4",
                            "posSuf": ""
                        }
                    ]
                },
                "id": "en-us",
                "pluralCat": function(n, opt_precision) {  var i = n | 0;  var vf = getVF(n, opt_precision);  if (i == 1 && vf.v == 0) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
            });
        }]);
    break;
    case "en-au" :
        angular.module("ngLocale", [], ["$provide", function($provide) {
            var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
            function getDecimals(n) {
                n = n + '';
                var i = n.indexOf('.');
                return (i == -1) ? 0 : n.length - i - 1;
            }

            function getVF(n, opt_precision) {
                var v = opt_precision;

                if (undefined === v) {
                    v = Math.min(getDecimals(n), 3);
                }

                var base = Math.pow(10, v);
                var f = ((n * base) | 0) % base;
                return {v: v, f: f};
            }

            $provide.value("$locale", {
                "DATETIME_FORMATS": {
                    "AMPMS": [
                        "am",
                        "pm"
                    ],
                    "DAY": [
                        "Sunday",
                        "Monday",
                        "Tuesday",
                        "Wednesday",
                        "Thursday",
                        "Friday",
                        "Saturday"
                    ],
                    "ERANAMES": [
                        "Before Christ",
                        "Anno Domini"
                    ],
                    "ERAS": [
                        "BC",
                        "AD"
                    ],
                    "MONTH": [
                        "January",
                        "February",
                        "March",
                        "April",
                        "May",
                        "June",
                        "July",
                        "August",
                        "September",
                        "October",
                        "November",
                        "December"
                    ],
                    "SHORTDAY": [
                        "Sun",
                        "Mon",
                        "Tue",
                        "Wed",
                        "Thu",
                        "Fri",
                        "Sat"
                    ],
                    "SHORTMONTH": [
                        "Jan",
                        "Feb",
                        "Mar",
                        "Apr",
                        "May",
                        "Jun",
                        "Jul",
                        "Aug",
                        "Sep",
                        "Oct",
                        "Nov",
                        "Dec"
                    ],
                    "fullDate": "EEEE, d MMMM y",
                    "longDate": "d MMMM y",
                    "medium": "d MMM y h:mm:ss a",
                    "mediumDate": "d MMM y",
                    "mediumTime": "h:mm:ss a",
                    "short": "d/MM/y h:mm a",
                    "shortDate": "d/MM/y",
                    "shortTime": "h:mm a"
                },
                "NUMBER_FORMATS": {
                    "CURRENCY_SYM": "$",
                    "DECIMAL_SEP": ".",
                    "GROUP_SEP": ",",
                    "PATTERNS": [
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 3,
                            "minFrac": 0,
                            "minInt": 1,
                            "negPre": "-",
                            "negSuf": "",
                            "posPre": "",
                            "posSuf": ""
                        },
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 2,
                            "minFrac": 2,
                            "minInt": 1,
                            "negPre": "\u00a4-",
                            "negSuf": "",
                            "posPre": "\u00a4",
                            "posSuf": ""
                        }
                    ]
                },
                "id": "en-au",
                "pluralCat": function(n, opt_precision) {  var i = n | 0;  var vf = getVF(n, opt_precision);  if (i == 1 && vf.v == 0) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
            });
        }]);
    break;
    case "en-gb" :
        angular.module("ngLocale", [], ["$provide", function($provide) {
            var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
            function getDecimals(n) {
                n = n + '';
                var i = n.indexOf('.');
                return (i == -1) ? 0 : n.length - i - 1;
            }

            function getVF(n, opt_precision) {
                var v = opt_precision;

                if (undefined === v) {
                    v = Math.min(getDecimals(n), 3);
                }

                var base = Math.pow(10, v);
                var f = ((n * base) | 0) % base;
                return {v: v, f: f};
            }

            $provide.value("$locale", {
                "DATETIME_FORMATS": {
                    "AMPMS": [
                        "am",
                        "pm"
                    ],
                    "DAY": [
                        "Sunday",
                        "Monday",
                        "Tuesday",
                        "Wednesday",
                        "Thursday",
                        "Friday",
                        "Saturday"
                    ],
                    "ERANAMES": [
                        "Before Christ",
                        "Anno Domini"
                    ],
                    "ERAS": [
                        "BC",
                        "AD"
                    ],
                    "MONTH": [
                        "January",
                        "February",
                        "March",
                        "April",
                        "May",
                        "June",
                        "July",
                        "August",
                        "September",
                        "October",
                        "November",
                        "December"
                    ],
                    "SHORTDAY": [
                        "Sun",
                        "Mon",
                        "Tue",
                        "Wed",
                        "Thu",
                        "Fri",
                        "Sat"
                    ],
                    "SHORTMONTH": [
                        "Jan",
                        "Feb",
                        "Mar",
                        "Apr",
                        "May",
                        "Jun",
                        "Jul",
                        "Aug",
                        "Sep",
                        "Oct",
                        "Nov",
                        "Dec"
                    ],
                    "fullDate": "EEEE, d MMMM y",
                    "longDate": "d MMMM y",
                    "medium": "d MMM y HH:mm:ss",
                    "mediumDate": "d MMM y",
                    "mediumTime": "HH:mm:ss",
                    "short": "dd/MM/y HH:mm",
                    "shortDate": "dd/MM/y",
                    "shortTime": "HH:mm"
                },
                "NUMBER_FORMATS": {
                    "CURRENCY_SYM": "\u00a3",
                    "DECIMAL_SEP": ".",
                    "GROUP_SEP": ",",
                    "PATTERNS": [
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 3,
                            "minFrac": 0,
                            "minInt": 1,
                            "negPre": "-",
                            "negSuf": "",
                            "posPre": "",
                            "posSuf": ""
                        },
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 2,
                            "minFrac": 2,
                            "minInt": 1,
                            "negPre": "\u00a4-",
                            "negSuf": "",
                            "posPre": "\u00a4",
                            "posSuf": ""
                        }
                    ]
                },
                "id": "en-gb",
                "pluralCat": function(n, opt_precision) {  var i = n | 0;  var vf = getVF(n, opt_precision);  if (i == 1 && vf.v == 0) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
            });
        }]);
        break;
    case "en-ie" :
        angular.module("ngLocale", [], ["$provide", function($provide) {
            var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
            function getDecimals(n) {
                n = n + '';
                var i = n.indexOf('.');
                return (i == -1) ? 0 : n.length - i - 1;
            }

            function getVF(n, opt_precision) {
                var v = opt_precision;

                if (undefined === v) {
                    v = Math.min(getDecimals(n), 3);
                }

                var base = Math.pow(10, v);
                var f = ((n * base) | 0) % base;
                return {v: v, f: f};
            }

            $provide.value("$locale", {
                "DATETIME_FORMATS": {
                    "AMPMS": [
                        "a.m.",
                        "p.m."
                    ],
                    "DAY": [
                        "Sunday",
                        "Monday",
                        "Tuesday",
                        "Wednesday",
                        "Thursday",
                        "Friday",
                        "Saturday"
                    ],
                    "ERANAMES": [
                        "Before Christ",
                        "Anno Domini"
                    ],
                    "ERAS": [
                        "BC",
                        "AD"
                    ],
                    "MONTH": [
                        "January",
                        "February",
                        "March",
                        "April",
                        "May",
                        "June",
                        "July",
                        "August",
                        "September",
                        "October",
                        "November",
                        "December"
                    ],
                    "SHORTDAY": [
                        "Sun",
                        "Mon",
                        "Tue",
                        "Wed",
                        "Thu",
                        "Fri",
                        "Sat"
                    ],
                    "SHORTMONTH": [
                        "Jan",
                        "Feb",
                        "Mar",
                        "Apr",
                        "May",
                        "Jun",
                        "Jul",
                        "Aug",
                        "Sep",
                        "Oct",
                        "Nov",
                        "Dec"
                    ],
                    "fullDate": "EEEE d MMMM y",
                    "longDate": "d MMMM y",
                    "medium": "d MMM y HH:mm:ss",
                    "mediumDate": "d MMM y",
                    "mediumTime": "HH:mm:ss",
                    "short": "dd/MM/y HH:mm",
                    "shortDate": "dd/MM/y",
                    "shortTime": "HH:mm"
                },
                "NUMBER_FORMATS": {
                    "CURRENCY_SYM": "\u20ac",
                    "DECIMAL_SEP": ".",
                    "GROUP_SEP": ",",
                    "PATTERNS": [
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 3,
                            "minFrac": 0,
                            "minInt": 1,
                            "negPre": "-",
                            "negSuf": "",
                            "posPre": "",
                            "posSuf": ""
                        },
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 2,
                            "minFrac": 2,
                            "minInt": 1,
                            "negPre": "\u00a4-",
                            "negSuf": "",
                            "posPre": "\u00a4",
                            "posSuf": ""
                        }
                    ]
                },
                "id": "en-ie",
                "pluralCat": function(n, opt_precision) {  var i = n | 0;  var vf = getVF(n, opt_precision);  if (i == 1 && vf.v == 0) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
            });
        }]);
        break;
    case "en-in" :
        angular.module("ngLocale", [], ["$provide", function($provide) {
            var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
            function getDecimals(n) {
                n = n + '';
                var i = n.indexOf('.');
                return (i == -1) ? 0 : n.length - i - 1;
            }

            function getVF(n, opt_precision) {
                var v = opt_precision;

                if (undefined === v) {
                    v = Math.min(getDecimals(n), 3);
                }

                var base = Math.pow(10, v);
                var f = ((n * base) | 0) % base;
                return {v: v, f: f};
            }

            $provide.value("$locale", {
                "DATETIME_FORMATS": {
                    "AMPMS": [
                        "am",
                        "pm"
                    ],
                    "DAY": [
                        "Sunday",
                        "Monday",
                        "Tuesday",
                        "Wednesday",
                        "Thursday",
                        "Friday",
                        "Saturday"
                    ],
                    "ERANAMES": [
                        "Before Christ",
                        "Anno Domini"
                    ],
                    "ERAS": [
                        "BC",
                        "AD"
                    ],
                    "MONTH": [
                        "January",
                        "February",
                        "March",
                        "April",
                        "May",
                        "June",
                        "July",
                        "August",
                        "September",
                        "October",
                        "November",
                        "December"
                    ],
                    "SHORTDAY": [
                        "Sun",
                        "Mon",
                        "Tue",
                        "Wed",
                        "Thu",
                        "Fri",
                        "Sat"
                    ],
                    "SHORTMONTH": [
                        "Jan",
                        "Feb",
                        "Mar",
                        "Apr",
                        "May",
                        "Jun",
                        "Jul",
                        "Aug",
                        "Sep",
                        "Oct",
                        "Nov",
                        "Dec"
                    ],
                    "fullDate": "EEEE d MMMM y",
                    "longDate": "d MMMM y",
                    "medium": "dd-MMM-y h:mm:ss a",
                    "mediumDate": "dd-MMM-y",
                    "mediumTime": "h:mm:ss a",
                    "short": "dd/MM/yy h:mm a",
                    "shortDate": "dd/MM/yy",
                    "shortTime": "h:mm a"
                },
                "NUMBER_FORMATS": {
                    "CURRENCY_SYM": "\u20b9",
                    "DECIMAL_SEP": ".",
                    "GROUP_SEP": ",",
                    "PATTERNS": [
                        {
                            "gSize": 2,
                            "lgSize": 3,
                            "maxFrac": 3,
                            "minFrac": 0,
                            "minInt": 1,
                            "negPre": "-",
                            "negSuf": "",
                            "posPre": "",
                            "posSuf": ""
                        },
                        {
                            "gSize": 2,
                            "lgSize": 3,
                            "maxFrac": 2,
                            "minFrac": 2,
                            "minInt": 1,
                            "negPre": "\u00a4\u00a0-",
                            "negSuf": "",
                            "posPre": "\u00a4\u00a0",
                            "posSuf": ""
                        }
                    ]
                },
                "id": "en-in",
                "pluralCat": function(n, opt_precision) {  var i = n | 0;  var vf = getVF(n, opt_precision);  if (i == 1 && vf.v == 0) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
            });
        }]);
        break;
    case "en" :
        angular.module("ngLocale", [], ["$provide", function($provide) {
            var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
            function getDecimals(n) {
                n = n + '';
                var i = n.indexOf('.');
                return (i == -1) ? 0 : n.length - i - 1;
            }

            function getVF(n, opt_precision) {
                var v = opt_precision;

                if (undefined === v) {
                    v = Math.min(getDecimals(n), 3);
                }

                var base = Math.pow(10, v);
                var f = ((n * base) | 0) % base;
                return {v: v, f: f};
            }

            $provide.value("$locale", {
                "DATETIME_FORMATS": {
                    "AMPMS": [
                        "AM",
                        "PM"
                    ],
                    "DAY": [
                        "Sunday",
                        "Monday",
                        "Tuesday",
                        "Wednesday",
                        "Thursday",
                        "Friday",
                        "Saturday"
                    ],
                    "ERANAMES": [
                        "Before Christ",
                        "Anno Domini"
                    ],
                    "ERAS": [
                        "BC",
                        "AD"
                    ],
                    "MONTH": [
                        "January",
                        "February",
                        "March",
                        "April",
                        "May",
                        "June",
                        "July",
                        "August",
                        "September",
                        "October",
                        "November",
                        "December"
                    ],
                    "SHORTDAY": [
                        "Sun",
                        "Mon",
                        "Tue",
                        "Wed",
                        "Thu",
                        "Fri",
                        "Sat"
                    ],
                    "SHORTMONTH": [
                        "Jan",
                        "Feb",
                        "Mar",
                        "Apr",
                        "May",
                        "Jun",
                        "Jul",
                        "Aug",
                        "Sep",
                        "Oct",
                        "Nov",
                        "Dec"
                    ],
                    "fullDate": "EEEE, MMMM d, y",
                    "longDate": "MMMM d, y",
                    "medium": "MMM d, y h:mm:ss a",
                    "mediumDate": "MMM d, y",
                    "mediumTime": "h:mm:ss a",
                    "short": "M/d/yy h:mm a",
                    "shortDate": "M/d/yy",
                    "shortTime": "h:mm a"
                },
                "NUMBER_FORMATS": {
                    "CURRENCY_SYM": "$",
                    "DECIMAL_SEP": ".",
                    "GROUP_SEP": ",",
                    "PATTERNS": [
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 3,
                            "minFrac": 0,
                            "minInt": 1,
                            "negPre": "-",
                            "negSuf": "",
                            "posPre": "",
                            "posSuf": ""
                        },
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 2,
                            "minFrac": 2,
                            "minInt": 1,
                            "negPre": "\u00a4-",
                            "negSuf": "",
                            "posPre": "\u00a4",
                            "posSuf": ""
                        }
                    ]
                },
                "id": "en",
                "pluralCat": function(n, opt_precision) {  var i = n | 0;  var vf = getVF(n, opt_precision);  if (i == 1 && vf.v == 0) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
            });
        }]);
        break;
    case "fr" :
        angular.module("ngLocale", [], ["$provide", function($provide) {
            var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
            $provide.value("$locale", {
                "DATETIME_FORMATS": {
                    "AMPMS": [
                        "AM",
                        "PM"
                    ],
                    "DAY": [
                        "dimanche",
                        "lundi",
                        "mardi",
                        "mercredi",
                        "jeudi",
                        "vendredi",
                        "samedi"
                    ],
                    "ERANAMES": [
                        "avant J\u00e9sus-Christ",
                        "apr\u00e8s J\u00e9sus-Christ"
                    ],
                    "ERAS": [
                        "av. J.-C.",
                        "ap. J.-C."
                    ],
                    "MONTH": [
                        "janvier",
                        "f\u00e9vrier",
                        "mars",
                        "avril",
                        "mai",
                        "juin",
                        "juillet",
                        "ao\u00fbt",
                        "septembre",
                        "octobre",
                        "novembre",
                        "d\u00e9cembre"
                    ],
                    "SHORTDAY": [
                        "dim.",
                        "lun.",
                        "mar.",
                        "mer.",
                        "jeu.",
                        "ven.",
                        "sam."
                    ],
                    "SHORTMONTH": [
                        "janv.",
                        "f\u00e9vr.",
                        "mars",
                        "avr.",
                        "mai",
                        "juin",
                        "juil.",
                        "ao\u00fbt",
                        "sept.",
                        "oct.",
                        "nov.",
                        "d\u00e9c."
                    ],
                    "fullDate": "EEEE d MMMM y",
                    "longDate": "d MMMM y",
                    "medium": "d MMM y HH:mm:ss",
                    "mediumDate": "d MMM y",
                    "mediumTime": "HH:mm:ss",
                    "short": "dd/MM/y HH:mm",
                    "shortDate": "dd/MM/y",
                    "shortTime": "HH:mm"
                },
                "NUMBER_FORMATS": {
                    "CURRENCY_SYM": "\u20ac",
                    "DECIMAL_SEP": ",",
                    "GROUP_SEP": "\u00a0",
                    "PATTERNS": [
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 3,
                            "minFrac": 0,
                            "minInt": 1,
                            "negPre": "-",
                            "negSuf": "",
                            "posPre": "",
                            "posSuf": ""
                        },
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 2,
                            "minFrac": 2,
                            "minInt": 1,
                            "negPre": "-",
                            "negSuf": "\u00a0\u00a4",
                            "posPre": "",
                            "posSuf": "\u00a0\u00a4"
                        }
                    ]
                },
                "id": "fr",
                "pluralCat": function(n, opt_precision) {  var i = n | 0;  if (i == 0 || i == 1) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
            });
        }]);
        break;
    case "fr-ca" :
        angular.module("ngLocale", [], ["$provide", function($provide) {
            var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
            $provide.value("$locale", {
                "DATETIME_FORMATS": {
                    "AMPMS": [
                        "AM",
                        "PM"
                    ],
                    "DAY": [
                        "dimanche",
                        "lundi",
                        "mardi",
                        "mercredi",
                        "jeudi",
                        "vendredi",
                        "samedi"
                    ],
                    "ERANAMES": [
                        "avant J\u00e9sus-Christ",
                        "apr\u00e8s J\u00e9sus-Christ"
                    ],
                    "ERAS": [
                        "av. J.-C.",
                        "ap. J.-C."
                    ],
                    "MONTH": [
                        "janvier",
                        "f\u00e9vrier",
                        "mars",
                        "avril",
                        "mai",
                        "juin",
                        "juillet",
                        "ao\u00fbt",
                        "septembre",
                        "octobre",
                        "novembre",
                        "d\u00e9cembre"
                    ],
                    "SHORTDAY": [
                        "dim.",
                        "lun.",
                        "mar.",
                        "mer.",
                        "jeu.",
                        "ven.",
                        "sam."
                    ],
                    "SHORTMONTH": [
                        "janv.",
                        "f\u00e9vr.",
                        "mars",
                        "avr.",
                        "mai",
                        "juin",
                        "juil.",
                        "ao\u00fbt",
                        "sept.",
                        "oct.",
                        "nov.",
                        "d\u00e9c."
                    ],
                    "fullDate": "EEEE d MMMM y",
                    "longDate": "d MMMM y",
                    "medium": "y-MM-dd HH:mm:ss",
                    "mediumDate": "y-MM-dd",
                    "mediumTime": "HH:mm:ss",
                    "short": "yy-MM-dd HH:mm",
                    "shortDate": "yy-MM-dd",
                    "shortTime": "HH:mm"
                },
                "NUMBER_FORMATS": {
                    "CURRENCY_SYM": "$",
                    "DECIMAL_SEP": ",",
                    "GROUP_SEP": "\u00a0",
                    "PATTERNS": [
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 3,
                            "minFrac": 0,
                            "minInt": 1,
                            "negPre": "-",
                            "negSuf": "",
                            "posPre": "",
                            "posSuf": ""
                        },
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 2,
                            "minFrac": 2,
                            "minInt": 1,
                            "negPre": "-",
                            "negSuf": "\u00a0\u00a4",
                            "posPre": "",
                            "posSuf": "\u00a0\u00a4"
                        }
                    ]
                },
                "id": "fr-ca",
                "pluralCat": function(n, opt_precision) {  var i = n | 0;  if (i == 0 || i == 1) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
            });
        }]);
        break;
    case "pt" :
        angular.module("ngLocale", [], ["$provide", function($provide) {
            var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
            $provide.value("$locale", {
                "DATETIME_FORMATS": {
                    "AMPMS": [
                        "AM",
                        "PM"
                    ],
                    "DAY": [
                        "domingo",
                        "segunda-feira",
                        "ter\u00e7a-feira",
                        "quarta-feira",
                        "quinta-feira",
                        "sexta-feira",
                        "s\u00e1bado"
                    ],
                    "ERANAMES": [
                        "Antes de Cristo",
                        "Ano do Senhor"
                    ],
                    "ERAS": [
                        "a.C.",
                        "d.C."
                    ],
                    "MONTH": [
                        "janeiro",
                        "fevereiro",
                        "mar\u00e7o",
                        "abril",
                        "maio",
                        "junho",
                        "julho",
                        "agosto",
                        "setembro",
                        "outubro",
                        "novembro",
                        "dezembro"
                    ],
                    "SHORTDAY": [
                        "dom",
                        "seg",
                        "ter",
                        "qua",
                        "qui",
                        "sex",
                        "s\u00e1b"
                    ],
                    "SHORTMONTH": [
                        "jan",
                        "fev",
                        "mar",
                        "abr",
                        "mai",
                        "jun",
                        "jul",
                        "ago",
                        "set",
                        "out",
                        "nov",
                        "dez"
                    ],
                    "fullDate": "EEEE, d 'de' MMMM 'de' y",
                    "longDate": "d 'de' MMMM 'de' y",
                    "medium": "d 'de' MMM 'de' y HH:mm:ss",
                    "mediumDate": "d 'de' MMM 'de' y",
                    "mediumTime": "HH:mm:ss",
                    "short": "dd/MM/yy HH:mm",
                    "shortDate": "dd/MM/yy",
                    "shortTime": "HH:mm"
                },
                "NUMBER_FORMATS": {
                    "CURRENCY_SYM": "R$",
                    "DECIMAL_SEP": ",",
                    "GROUP_SEP": ".",
                    "PATTERNS": [
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 3,
                            "minFrac": 0,
                            "minInt": 1,
                            "negPre": "-",
                            "negSuf": "",
                            "posPre": "",
                            "posSuf": ""
                        },
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 2,
                            "minFrac": 2,
                            "minInt": 1,
                            "negPre": "\u00a4-",
                            "negSuf": "",
                            "posPre": "\u00a4",
                            "posSuf": ""
                        }
                    ]
                },
                "id": "pt",
                "pluralCat": function(n, opt_precision) {  if (n >= 0 && n <= 2 && n != 2) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
            });
        }]);
        break;
    case "es" :
        angular.module("ngLocale", [], ["$provide", function($provide) {
            var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
            $provide.value("$locale", {
                "DATETIME_FORMATS": {
                    "AMPMS": [
                        "a. m.",
                        "p. m."
                    ],
                    "DAY": [
                        "domingo",
                        "lunes",
                        "martes",
                        "mi\u00e9rcoles",
                        "jueves",
                        "viernes",
                        "s\u00e1bado"
                    ],
                    "ERANAMES": [
                        "antes de Cristo",
                        "anno D\u00f3mini"
                    ],
                    "ERAS": [
                        "a. C.",
                        "d. C."
                    ],
                    "MONTH": [
                        "enero",
                        "febrero",
                        "marzo",
                        "abril",
                        "mayo",
                        "junio",
                        "julio",
                        "agosto",
                        "septiembre",
                        "octubre",
                        "noviembre",
                        "diciembre"
                    ],
                    "SHORTDAY": [
                        "dom.",
                        "lun.",
                        "mar.",
                        "mi\u00e9.",
                        "jue.",
                        "vie.",
                        "s\u00e1b."
                    ],
                    "SHORTMONTH": [
                        "ene.",
                        "feb.",
                        "mar.",
                        "abr.",
                        "may.",
                        "jun.",
                        "jul.",
                        "ago.",
                        "sept.",
                        "oct.",
                        "nov.",
                        "dic."
                    ],
                    "fullDate": "EEEE, d 'de' MMMM 'de' y",
                    "longDate": "d 'de' MMMM 'de' y",
                    "medium": "d 'de' MMM 'de' y H:mm:ss",
                    "mediumDate": "d 'de' MMM 'de' y",
                    "mediumTime": "H:mm:ss",
                    "short": "d/M/yy H:mm",
                    "shortDate": "d/M/yy",
                    "shortTime": "H:mm"
                },
                "NUMBER_FORMATS": {
                    "CURRENCY_SYM": "\u20ac",
                    "DECIMAL_SEP": ",",
                    "GROUP_SEP": ".",
                    "PATTERNS": [
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 3,
                            "minFrac": 0,
                            "minInt": 1,
                            "negPre": "-",
                            "negSuf": "",
                            "posPre": "",
                            "posSuf": ""
                        },
                        {
                            "gSize": 3,
                            "lgSize": 3,
                            "maxFrac": 2,
                            "minFrac": 2,
                            "minInt": 1,
                            "negPre": "-",
                            "negSuf": "\u00a0\u00a4",
                            "posPre": "",
                            "posSuf": "\u00a0\u00a4"
                        }
                    ]
                },
                "id": "es",
                "pluralCat": function(n, opt_precision) {  if (n == 1) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
            });
        }]);
        break;
    case "es-co":
        angular.module("ngLocale", [], ["$provide", function($provide) {
        var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
        $provide.value("$locale", {
          "DATETIME_FORMATS": {
            "AMPMS": [
              "a. m.",
              "p. m."
            ],
            "DAY": [
              "domingo",
              "lunes",
              "martes",
              "mi\u00e9rcoles",
              "jueves",
              "viernes",
              "s\u00e1bado"
            ],
            "ERANAMES": [
              "antes de Cristo",
              "despu\u00e9s de Cristo"
            ],
            "ERAS": [
              "a. C.",
              "d. C."
            ],
            "FIRSTDAYOFWEEK": 6,
            "MONTH": [
              "enero",
              "febrero",
              "marzo",
              "abril",
              "mayo",
              "junio",
              "julio",
              "agosto",
              "septiembre",
              "octubre",
              "noviembre",
              "diciembre"
            ],
            "SHORTDAY": [
              "dom.",
              "lun.",
              "mar.",
              "mi\u00e9.",
              "jue.",
              "vie.",
              "s\u00e1b."
            ],
            "SHORTMONTH": [
              "ene.",
              "feb.",
              "mar.",
              "abr.",
              "may.",
              "jun.",
              "jul.",
              "ago.",
              "sept.",
              "oct.",
              "nov.",
              "dic."
            ],
            "STANDALONEMONTH": [
              "Enero",
              "Febrero",
              "Marzo",
              "Abril",
              "Mayo",
              "Junio",
              "Julio",
              "Agosto",
              "Septiembre",
              "Octubre",
              "Noviembre",
              "Diciembre"
            ],
            "WEEKENDRANGE": [
              5,
              6
            ],
            "fullDate": "EEEE, d 'de' MMMM 'de' y",
            "longDate": "d 'de' MMMM 'de' y",
            "medium": "d/MM/y h:mm:ss a",
            "mediumDate": "d/MM/y",
            "mediumTime": "h:mm:ss a",
            "short": "d/MM/yy h:mm a",
            "shortDate": "d/MM/yy",
            "shortTime": "h:mm a"
          },
          "NUMBER_FORMATS": {
            "CURRENCY_SYM": "$",
            "DECIMAL_SEP": ",",
            "GROUP_SEP": ".",
            "PATTERNS": [
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 3,
                "minFrac": 0,
                "minInt": 1,
                "negPre": "-",
                "negSuf": "",
                "posPre": "",
                "posSuf": ""
              },
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 2,
                "minFrac": 2,
                "minInt": 1,
                "negPre": "-\u00a4",
                "negSuf": "",
                "posPre": "\u00a4",
                "posSuf": ""
              }
            ]
          },
          "id": "es-co",
          "pluralCat": function(n, opt_precision) {  if (n == 1) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
        });
        }]);
        break;

    case "es-mx":
        angular.module("ngLocale", [], ["$provide", function($provide) {
        var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
        $provide.value("$locale", {
          "DATETIME_FORMATS": {
            "AMPMS": [
              "a.m.",
              "p.m."
            ],
            "DAY": [
              "domingo",
              "lunes",
              "martes",
              "mi\u00e9rcoles",
              "jueves",
              "viernes",
              "s\u00e1bado"
            ],
            "ERANAMES": [
              "antes de Cristo",
              "despu\u00e9s de Cristo"
            ],
            "ERAS": [
              "a. C.",
              "d. C."
            ],
            "FIRSTDAYOFWEEK": 6,
            "MONTH": [
              "enero",
              "febrero",
              "marzo",
              "abril",
              "mayo",
              "junio",
              "julio",
              "agosto",
              "septiembre",
              "octubre",
              "noviembre",
              "diciembre"
            ],
            "SHORTDAY": [
              "dom.",
              "lun.",
              "mar.",
              "mi\u00e9.",
              "jue.",
              "vie.",
              "s\u00e1b."
            ],
            "SHORTMONTH": [
              "ene",
              "feb",
              "mar",
              "abr",
              "may",
              "jun",
              "jul",
              "ago",
              "sep",
              "oct",
              "nov",
              "dic"
            ],
            "STANDALONEMONTH": [
              "Enero",
              "Febrero",
              "Marzo",
              "Abril",
              "Mayo",
              "Junio",
              "Julio",
              "Agosto",
              "Septiembre",
              "Octubre",
              "Noviembre",
              "Diciembre"
            ],
            "WEEKENDRANGE": [
              5,
              6
            ],
            "fullDate": "EEEE, d 'de' MMMM 'de' y",
            "longDate": "d 'de' MMMM 'de' y",
            "medium": "dd/MM/y h:mm:ss a",
            "mediumDate": "dd/MM/y",
            "mediumTime": "h:mm:ss a",
            "short": "dd/MM/yy h:mm a",
            "shortDate": "dd/MM/yy",
            "shortTime": "h:mm a"
          },
          "NUMBER_FORMATS": {
            "CURRENCY_SYM": "$",
            "DECIMAL_SEP": ".",
            "GROUP_SEP": ",",
            "PATTERNS": [
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 3,
                "minFrac": 0,
                "minInt": 1,
                "negPre": "-",
                "negSuf": "",
                "posPre": "",
                "posSuf": ""
              },
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 2,
                "minFrac": 2,
                "minInt": 1,
                "negPre": "-\u00a4",
                "negSuf": "",
                "posPre": "\u00a4",
                "posSuf": ""
              }
            ]
          },
          "id": "es-mx",
          "pluralCat": function(n, opt_precision) {  if (n == 1) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
        });
        }]);
        break;

    case "es-pr":
        angular.module("ngLocale", [], ["$provide", function($provide) {
        var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
        $provide.value("$locale", {
          "DATETIME_FORMATS": {
            "AMPMS": [
              "a. m.",
              "p. m."
            ],
            "DAY": [
              "domingo",
              "lunes",
              "martes",
              "mi\u00e9rcoles",
              "jueves",
              "viernes",
              "s\u00e1bado"
            ],
            "ERANAMES": [
              "antes de Cristo",
              "despu\u00e9s de Cristo"
            ],
            "ERAS": [
              "a. C.",
              "d. C."
            ],
            "FIRSTDAYOFWEEK": 6,
            "MONTH": [
              "enero",
              "febrero",
              "marzo",
              "abril",
              "mayo",
              "junio",
              "julio",
              "agosto",
              "septiembre",
              "octubre",
              "noviembre",
              "diciembre"
            ],
            "SHORTDAY": [
              "dom.",
              "lun.",
              "mar.",
              "mi\u00e9.",
              "jue.",
              "vie.",
              "s\u00e1b."
            ],
            "SHORTMONTH": [
              "ene.",
              "feb.",
              "mar.",
              "abr.",
              "may.",
              "jun.",
              "jul.",
              "ago.",
              "sept.",
              "oct.",
              "nov.",
              "dic."
            ],
            "STANDALONEMONTH": [
              "Enero",
              "Febrero",
              "Marzo",
              "Abril",
              "Mayo",
              "Junio",
              "Julio",
              "Agosto",
              "Septiembre",
              "Octubre",
              "Noviembre",
              "Diciembre"
            ],
            "WEEKENDRANGE": [
              5,
              6
            ],
            "fullDate": "EEEE, d 'de' MMMM 'de' y",
            "longDate": "d 'de' MMMM 'de' y",
            "medium": "MM/dd/y h:mm:ss a",
            "mediumDate": "MM/dd/y",
            "mediumTime": "h:mm:ss a",
            "short": "MM/dd/yy h:mm a",
            "shortDate": "MM/dd/yy",
            "shortTime": "h:mm a"
          },
          "NUMBER_FORMATS": {
            "CURRENCY_SYM": "$",
            "DECIMAL_SEP": ".",
            "GROUP_SEP": ",",
            "PATTERNS": [
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 3,
                "minFrac": 0,
                "minInt": 1,
                "negPre": "-",
                "negSuf": "",
                "posPre": "",
                "posSuf": ""
              },
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 2,
                "minFrac": 2,
                "minInt": 1,
                "negPre": "-\u00a4",
                "negSuf": "",
                "posPre": "\u00a4",
                "posSuf": ""
              }
            ]
          },
          "id": "es-pr",
          "pluralCat": function(n, opt_precision) {  if (n == 1) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
        });
        }]);
        break;
    case "es-cl":
        'use strict';
        angular.module("ngLocale", [], ["$provide", function($provide) {
        var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
        $provide.value("$locale", {
          "DATETIME_FORMATS": {
            "AMPMS": [
              "a. m.",
              "p. m."
            ],
            "DAY": [
              "domingo",
              "lunes",
              "martes",
              "mi\u00e9rcoles",
              "jueves",
              "viernes",
              "s\u00e1bado"
            ],
            "ERANAMES": [
              "antes de Cristo",
              "despu\u00e9s de Cristo"
            ],
            "ERAS": [
              "a. C.",
              "d. C."
            ],
            "FIRSTDAYOFWEEK": 0,
            "MONTH": [
              "enero",
              "febrero",
              "marzo",
              "abril",
              "mayo",
              "junio",
              "julio",
              "agosto",
              "septiembre",
              "octubre",
              "noviembre",
              "diciembre"
            ],
            "SHORTDAY": [
              "dom.",
              "lun.",
              "mar.",
              "mi\u00e9.",
              "jue.",
              "vie.",
              "s\u00e1b."
            ],
            "SHORTMONTH": [
              "ene.",
              "feb.",
              "mar.",
              "abr.",
              "may.",
              "jun.",
              "jul.",
              "ago.",
              "sept.",
              "oct.",
              "nov.",
              "dic."
            ],
            "STANDALONEMONTH": [
              "Enero",
              "Febrero",
              "Marzo",
              "Abril",
              "Mayo",
              "Junio",
              "Julio",
              "Agosto",
              "Septiembre",
              "Octubre",
              "Noviembre",
              "Diciembre"
            ],
            "WEEKENDRANGE": [
              5,
              6
            ],
            "fullDate": "EEEE, d 'de' MMMM 'de' y",
            "longDate": "d 'de' MMMM 'de' y",
            "medium": "dd-MM-y h:mm:ss a",
            "mediumDate": "dd-MM-y",
            "mediumTime": "h:mm:ss a",
            "short": "dd-MM-yy h:mm a",
            "shortDate": "dd-MM-yy",
            "shortTime": "h:mm a"
          },
          "NUMBER_FORMATS": {
            "CURRENCY_SYM": "$",
            "DECIMAL_SEP": ",",
            "GROUP_SEP": ".",
            "PATTERNS": [
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 3,
                "minFrac": 0,
                "minInt": 1,
                "negPre": "-",
                "negSuf": "",
                "posPre": "",
                "posSuf": ""
              },
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 2,
                "minFrac": 2,
                "minInt": 1,
                "negPre": "\u00a4-",
                "negSuf": "",
                "posPre": "\u00a4",
                "posSuf": ""
              }
            ]
          },
          "id": "es-cl",
          "pluralCat": function(n, opt_precision) {  if (n == 1) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
        });
        }]);
        break;

    case "es-cr":
        'use strict';
        angular.module("ngLocale", [], ["$provide", function($provide) {
        var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
        $provide.value("$locale", {
          "DATETIME_FORMATS": {
            "AMPMS": [
              "a. m.",
              "p. m."
            ],
            "DAY": [
              "domingo",
              "lunes",
              "martes",
              "mi\u00e9rcoles",
              "jueves",
              "viernes",
              "s\u00e1bado"
            ],
            "ERANAMES": [
              "antes de Cristo",
              "despu\u00e9s de Cristo"
            ],
            "ERAS": [
              "a. C.",
              "d. C."
            ],
            "FIRSTDAYOFWEEK": 0,
            "MONTH": [
              "enero",
              "febrero",
              "marzo",
              "abril",
              "mayo",
              "junio",
              "julio",
              "agosto",
              "septiembre",
              "octubre",
              "noviembre",
              "diciembre"
            ],
            "SHORTDAY": [
              "dom.",
              "lun.",
              "mar.",
              "mi\u00e9.",
              "jue.",
              "vie.",
              "s\u00e1b."
            ],
            "SHORTMONTH": [
              "ene.",
              "feb.",
              "mar.",
              "abr.",
              "may.",
              "jun.",
              "jul.",
              "ago.",
              "sept.",
              "oct.",
              "nov.",
              "dic."
            ],
            "STANDALONEMONTH": [
              "Enero",
              "Febrero",
              "Marzo",
              "Abril",
              "Mayo",
              "Junio",
              "Julio",
              "Agosto",
              "Septiembre",
              "Octubre",
              "Noviembre",
              "Diciembre"
            ],
            "WEEKENDRANGE": [
              5,
              6
            ],
            "fullDate": "EEEE, d 'de' MMMM 'de' y",
            "longDate": "d 'de' MMMM 'de' y",
            "medium": "d MMM y h:mm:ss a",
            "mediumDate": "d MMM y",
            "mediumTime": "h:mm:ss a",
            "short": "d/M/yy h:mm a",
            "shortDate": "d/M/yy",
            "shortTime": "h:mm a"
          },
          "NUMBER_FORMATS": {
            "CURRENCY_SYM": "\u20a1",
            "DECIMAL_SEP": ",",
            "GROUP_SEP": ".",
            "PATTERNS": [
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 3,
                "minFrac": 0,
                "minInt": 1,
                "negPre": "-",
                "negSuf": "",
                "posPre": "",
                "posSuf": ""
              },
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 2,
                "minFrac": 2,
                "minInt": 1,
                "negPre": "-\u00a4",
                "negSuf": "",
                "posPre": "\u00a4",
                "posSuf": ""
              }
            ]
          },
          "id": "es-cr",
          "pluralCat": function(n, opt_precision) {  if (n == 1) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
        });
        }]);
        break;

    case "es-do":
        'use strict';
        angular.module("ngLocale", [], ["$provide", function($provide) {
        var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
        $provide.value("$locale", {
          "DATETIME_FORMATS": {
            "AMPMS": [
              "a. m.",
              "p. m."
            ],
            "DAY": [
              "domingo",
              "lunes",
              "martes",
              "mi\u00e9rcoles",
              "jueves",
              "viernes",
              "s\u00e1bado"
            ],
            "ERANAMES": [
              "antes de Cristo",
              "despu\u00e9s de Cristo"
            ],
            "ERAS": [
              "a. C.",
              "d. C."
            ],
            "FIRSTDAYOFWEEK": 6,
            "MONTH": [
              "enero",
              "febrero",
              "marzo",
              "abril",
              "mayo",
              "junio",
              "julio",
              "agosto",
              "septiembre",
              "octubre",
              "noviembre",
              "diciembre"
            ],
            "SHORTDAY": [
              "dom.",
              "lun.",
              "mar.",
              "mi\u00e9.",
              "jue.",
              "vie.",
              "s\u00e1b."
            ],
            "SHORTMONTH": [
              "ene.",
              "feb.",
              "mar.",
              "abr.",
              "may.",
              "jun.",
              "jul.",
              "ago.",
              "sept.",
              "oct.",
              "nov.",
              "dic."
            ],
            "STANDALONEMONTH": [
              "Enero",
              "Febrero",
              "Marzo",
              "Abril",
              "Mayo",
              "Junio",
              "Julio",
              "Agosto",
              "Septiembre",
              "Octubre",
              "Noviembre",
              "Diciembre"
            ],
            "WEEKENDRANGE": [
              5,
              6
            ],
            "fullDate": "EEEE, d 'de' MMMM 'de' y",
            "longDate": "d 'de' MMMM 'de' y",
            "medium": "d MMM y h:mm:ss a",
            "mediumDate": "d MMM y",
            "mediumTime": "h:mm:ss a",
            "short": "d/M/yy h:mm a",
            "shortDate": "d/M/yy",
            "shortTime": "h:mm a"
          },
          "NUMBER_FORMATS": {
            "CURRENCY_SYM": "$",
            "DECIMAL_SEP": ".",
            "GROUP_SEP": ",",
            "PATTERNS": [
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 3,
                "minFrac": 0,
                "minInt": 1,
                "negPre": "-",
                "negSuf": "",
                "posPre": "",
                "posSuf": ""
              },
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 2,
                "minFrac": 2,
                "minInt": 1,
                "negPre": "-\u00a4",
                "negSuf": "",
                "posPre": "\u00a4",
                "posSuf": ""
              }
            ]
          },
          "id": "es-do",
          "pluralCat": function(n, opt_precision) {  if (n == 1) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
        });
        }]);
        break;

    case "es-ec":
        'use strict';
        angular.module("ngLocale", [], ["$provide", function($provide) {
        var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
        $provide.value("$locale", {
          "DATETIME_FORMATS": {
            "AMPMS": [
              "a. m.",
              "p. m."
            ],
            "DAY": [
              "domingo",
              "lunes",
              "martes",
              "mi\u00e9rcoles",
              "jueves",
              "viernes",
              "s\u00e1bado"
            ],
            "ERANAMES": [
              "antes de Cristo",
              "despu\u00e9s de Cristo"
            ],
            "ERAS": [
              "a. C.",
              "d. C."
            ],
            "FIRSTDAYOFWEEK": 0,
            "MONTH": [
              "enero",
              "febrero",
              "marzo",
              "abril",
              "mayo",
              "junio",
              "julio",
              "agosto",
              "septiembre",
              "octubre",
              "noviembre",
              "diciembre"
            ],
            "SHORTDAY": [
              "dom.",
              "lun.",
              "mar.",
              "mi\u00e9.",
              "jue.",
              "vie.",
              "s\u00e1b."
            ],
            "SHORTMONTH": [
              "ene.",
              "feb.",
              "mar.",
              "abr.",
              "may.",
              "jun.",
              "jul.",
              "ago.",
              "sept.",
              "oct.",
              "nov.",
              "dic."
            ],
            "STANDALONEMONTH": [
              "Enero",
              "Febrero",
              "Marzo",
              "Abril",
              "Mayo",
              "Junio",
              "Julio",
              "Agosto",
              "Septiembre",
              "Octubre",
              "Noviembre",
              "Diciembre"
            ],
            "WEEKENDRANGE": [
              5,
              6
            ],
            "fullDate": "EEEE, d 'de' MMMM 'de' y",
            "longDate": "d 'de' MMMM 'de' y",
            "medium": "d MMM y h:mm:ss a",
            "mediumDate": "d MMM y",
            "mediumTime": "h:mm:ss a",
            "short": "d/M/yy h:mm a",
            "shortDate": "d/M/yy",
            "shortTime": "h:mm a"
          },
          "NUMBER_FORMATS": {
            "CURRENCY_SYM": "$",
            "DECIMAL_SEP": ",",
            "GROUP_SEP": ".",
            "PATTERNS": [
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 3,
                "minFrac": 0,
                "minInt": 1,
                "negPre": "-",
                "negSuf": "",
                "posPre": "",
                "posSuf": ""
              },
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 2,
                "minFrac": 2,
                "minInt": 1,
                "negPre": "-\u00a4",
                "negSuf": "",
                "posPre": "\u00a4",
                "posSuf": ""
              }
            ]
          },
          "id": "es-ec",
          "pluralCat": function(n, opt_precision) {  if (n == 1) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
        });
        }]);
        break;

    case "es-pe":
        'use strict';
        angular.module("ngLocale", [], ["$provide", function($provide) {
        var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
        $provide.value("$locale", {
          "DATETIME_FORMATS": {
            "AMPMS": [
              "a. m.",
              "p. m."
            ],
            "DAY": [
              "domingo",
              "lunes",
              "martes",
              "mi\u00e9rcoles",
              "jueves",
              "viernes",
              "s\u00e1bado"
            ],
            "ERANAMES": [
              "antes de Cristo",
              "despu\u00e9s de Cristo"
            ],
            "ERAS": [
              "a. C.",
              "d. C."
            ],
            "FIRSTDAYOFWEEK": 6,
            "MONTH": [
              "enero",
              "febrero",
              "marzo",
              "abril",
              "mayo",
              "junio",
              "julio",
              "agosto",
              "setiembre",
              "octubre",
              "noviembre",
              "diciembre"
            ],
            "SHORTDAY": [
              "dom.",
              "lun.",
              "mar.",
              "mi\u00e9.",
              "jue.",
              "vie.",
              "s\u00e1b."
            ],
            "SHORTMONTH": [
              "ene.",
              "feb.",
              "mar.",
              "abr.",
              "may.",
              "jun.",
              "jul.",
              "ago.",
              "set.",
              "oct.",
              "nov.",
              "dic."
            ],
            "STANDALONEMONTH": [
              "Enero",
              "Febrero",
              "Marzo",
              "Abril",
              "Mayo",
              "Junio",
              "Julio",
              "Agosto",
              "Setiembre",
              "Octubre",
              "Noviembre",
              "Diciembre"
            ],
            "WEEKENDRANGE": [
              5,
              6
            ],
            "fullDate": "EEEE, d 'de' MMMM 'de' y",
            "longDate": "d 'de' MMMM 'de' y",
            "medium": "d MMM y h:mm:ss a",
            "mediumDate": "d MMM y",
            "mediumTime": "h:mm:ss a",
            "short": "d/MM/yy h:mm a",
            "shortDate": "d/MM/yy",
            "shortTime": "h:mm a"
          },
          "NUMBER_FORMATS": {
            "CURRENCY_SYM": "S/.",
            "DECIMAL_SEP": ".",
            "GROUP_SEP": ",",
            "PATTERNS": [
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 3,
                "minFrac": 0,
                "minInt": 1,
                "negPre": "-",
                "negSuf": "",
                "posPre": "",
                "posSuf": ""
              },
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 2,
                "minFrac": 2,
                "minInt": 1,
                "negPre": "-\u00a4",
                "negSuf": "",
                "posPre": "\u00a4",
                "posSuf": ""
              }
            ]
          },
          "id": "es-pe",
          "pluralCat": function(n, opt_precision) {  if (n == 1) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
        });
        }]);
        break;

    case "es-ve":
        'use strict';
        angular.module("ngLocale", [], ["$provide", function($provide) {
        var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
        $provide.value("$locale", {
          "DATETIME_FORMATS": {
            "AMPMS": [
              "a. m.",
              "p. m."
            ],
            "DAY": [
              "domingo",
              "lunes",
              "martes",
              "mi\u00e9rcoles",
              "jueves",
              "viernes",
              "s\u00e1bado"
            ],
            "ERANAMES": [
              "antes de Cristo",
              "despu\u00e9s de Cristo"
            ],
            "ERAS": [
              "a. C.",
              "d. C."
            ],
            "FIRSTDAYOFWEEK": 6,
            "MONTH": [
              "enero",
              "febrero",
              "marzo",
              "abril",
              "mayo",
              "junio",
              "julio",
              "agosto",
              "septiembre",
              "octubre",
              "noviembre",
              "diciembre"
            ],
            "SHORTDAY": [
              "dom.",
              "lun.",
              "mar.",
              "mi\u00e9.",
              "jue.",
              "vie.",
              "s\u00e1b."
            ],
            "SHORTMONTH": [
              "ene.",
              "feb.",
              "mar.",
              "abr.",
              "may.",
              "jun.",
              "jul.",
              "ago.",
              "sept.",
              "oct.",
              "nov.",
              "dic."
            ],
            "STANDALONEMONTH": [
              "Enero",
              "Febrero",
              "Marzo",
              "Abril",
              "Mayo",
              "Junio",
              "Julio",
              "Agosto",
              "Septiembre",
              "Octubre",
              "Noviembre",
              "Diciembre"
            ],
            "WEEKENDRANGE": [
              5,
              6
            ],
            "fullDate": "EEEE, d 'de' MMMM 'de' y",
            "longDate": "d 'de' MMMM 'de' y",
            "medium": "d MMM y h:mm:ss a",
            "mediumDate": "d MMM y",
            "mediumTime": "h:mm:ss a",
            "short": "d/M/yy h:mm a",
            "shortDate": "d/M/yy",
            "shortTime": "h:mm a"
          },
          "NUMBER_FORMATS": {
            "CURRENCY_SYM": "Bs",
            "DECIMAL_SEP": ",",
            "GROUP_SEP": ".",
            "PATTERNS": [
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 3,
                "minFrac": 0,
                "minInt": 1,
                "negPre": "-",
                "negSuf": "",
                "posPre": "",
                "posSuf": ""
              },
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 2,
                "minFrac": 2,
                "minInt": 1,
                "negPre": "-\u00a4",
                "negSuf": "",
                "posPre": "\u00a4",
                "posSuf": ""
              }
            ]
          },
          "id": "es-ve",
          "pluralCat": function(n, opt_precision) {  if (n == 1) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
        });
        }]);
        break;
    case "es-ar":
        'use strict';
        angular.module("ngLocale", [], ["$provide", function($provide) {
        var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
        $provide.value("$locale", {
          "DATETIME_FORMATS": {
            "AMPMS": [
              "a. m.",
              "p. m."
            ],
            "DAY": [
              "domingo",
              "lunes",
              "martes",
              "mi\u00e9rcoles",
              "jueves",
              "viernes",
              "s\u00e1bado"
            ],
            "ERANAMES": [
              "antes de Cristo",
              "despu\u00e9s de Cristo"
            ],
            "ERAS": [
              "a. C.",
              "d. C."
            ],
            "FIRSTDAYOFWEEK": 6,
            "MONTH": [
              "enero",
              "febrero",
              "marzo",
              "abril",
              "mayo",
              "junio",
              "julio",
              "agosto",
              "septiembre",
              "octubre",
              "noviembre",
              "diciembre"
            ],
            "SHORTDAY": [
              "dom.",
              "lun.",
              "mar.",
              "mi\u00e9.",
              "jue.",
              "vie.",
              "s\u00e1b."
            ],
            "SHORTMONTH": [
              "ene.",
              "feb.",
              "mar.",
              "abr.",
              "may.",
              "jun.",
              "jul.",
              "ago.",
              "sept.",
              "oct.",
              "nov.",
              "dic."
            ],
            "STANDALONEMONTH": [
              "Enero",
              "Febrero",
              "Marzo",
              "Abril",
              "Mayo",
              "Junio",
              "Julio",
              "Agosto",
              "Septiembre",
              "Octubre",
              "Noviembre",
              "Diciembre"
            ],
            "WEEKENDRANGE": [
              5,
              6
            ],
            "fullDate": "EEEE, d 'de' MMMM 'de' y",
            "longDate": "d 'de' MMMM 'de' y",
            "medium": "d MMM y h:mm:ss a",
            "mediumDate": "d MMM y",
            "mediumTime": "h:mm:ss a",
            "short": "d/M/yy h:mm a",
            "shortDate": "d/M/yy",
            "shortTime": "h:mm a"
          },
          "NUMBER_FORMATS": {
            "CURRENCY_SYM": "$",
            "DECIMAL_SEP": ",",
            "GROUP_SEP": ".",
            "PATTERNS": [
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 3,
                "minFrac": 0,
                "minInt": 1,
                "negPre": "-",
                "negSuf": "",
                "posPre": "",
                "posSuf": ""
              },
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 2,
                "minFrac": 2,
                "minInt": 1,
                "negPre": "-\u00a4",
                "negSuf": "",
                "posPre": "\u00a4",
                "posSuf": ""
              }
            ]
          },
          "id": "es-ar",
          "pluralCat": function(n, opt_precision) {  if (n == 1) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
        });
        }]);
        break;
    case "es-gt":
        'use strict';
        angular.module("ngLocale", [], ["$provide", function($provide) {
        var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
        $provide.value("$locale", {
          "DATETIME_FORMATS": {
            "AMPMS": [
              "a. m.",
              "p. m."
            ],
            "DAY": [
              "domingo",
              "lunes",
              "martes",
              "mi\u00e9rcoles",
              "jueves",
              "viernes",
              "s\u00e1bado"
            ],
            "ERANAMES": [
              "antes de Cristo",
              "despu\u00e9s de Cristo"
            ],
            "ERAS": [
              "a. C.",
              "d. C."
            ],
            "FIRSTDAYOFWEEK": 6,
            "MONTH": [
              "enero",
              "febrero",
              "marzo",
              "abril",
              "mayo",
              "junio",
              "julio",
              "agosto",
              "septiembre",
              "octubre",
              "noviembre",
              "diciembre"
            ],
            "SHORTDAY": [
              "dom.",
              "lun.",
              "mar.",
              "mi\u00e9.",
              "jue.",
              "vie.",
              "s\u00e1b."
            ],
            "SHORTMONTH": [
              "ene.",
              "feb.",
              "mar.",
              "abr.",
              "may.",
              "jun.",
              "jul.",
              "ago.",
              "sept.",
              "oct.",
              "nov.",
              "dic."
            ],
            "STANDALONEMONTH": [
              "Enero",
              "Febrero",
              "Marzo",
              "Abril",
              "Mayo",
              "Junio",
              "Julio",
              "Agosto",
              "Septiembre",
              "Octubre",
              "Noviembre",
              "Diciembre"
            ],
            "WEEKENDRANGE": [
              5,
              6
            ],
            "fullDate": "EEEE, d 'de' MMMM 'de' y",
            "longDate": "d 'de' MMMM 'de' y",
            "medium": "d/MM/y h:mm:ss a",
            "mediumDate": "d/MM/y",
            "mediumTime": "h:mm:ss a",
            "short": "d/MM/yy h:mm a",
            "shortDate": "d/MM/yy",
            "shortTime": "h:mm a"
          },
          "NUMBER_FORMATS": {
            "CURRENCY_SYM": "Q",
            "DECIMAL_SEP": ".",
            "GROUP_SEP": ",",
            "PATTERNS": [
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 3,
                "minFrac": 0,
                "minInt": 1,
                "negPre": "-",
                "negSuf": "",
                "posPre": "",
                "posSuf": ""
              },
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 2,
                "minFrac": 2,
                "minInt": 1,
                "negPre": "-\u00a4",
                "negSuf": "",
                "posPre": "\u00a4",
                "posSuf": ""
              }
            ]
          },
          "id": "es-gt",
          "pluralCat": function(n, opt_precision) {  if (n == 1) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
        });
        }]);
        break;
    case "es-pa":
        'use strict';
        angular.module("ngLocale", [], ["$provide", function($provide) {
        var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
        $provide.value("$locale", {
          "DATETIME_FORMATS": {
            "AMPMS": [
              "a. m.",
              "p. m."
            ],
            "DAY": [
              "domingo",
              "lunes",
              "martes",
              "mi\u00e9rcoles",
              "jueves",
              "viernes",
              "s\u00e1bado"
            ],
            "ERANAMES": [
              "antes de Cristo",
              "despu\u00e9s de Cristo"
            ],
            "ERAS": [
              "a. C.",
              "d. C."
            ],
            "FIRSTDAYOFWEEK": 6,
            "MONTH": [
              "enero",
              "febrero",
              "marzo",
              "abril",
              "mayo",
              "junio",
              "julio",
              "agosto",
              "septiembre",
              "octubre",
              "noviembre",
              "diciembre"
            ],
            "SHORTDAY": [
              "dom.",
              "lun.",
              "mar.",
              "mi\u00e9.",
              "jue.",
              "vie.",
              "s\u00e1b."
            ],
            "SHORTMONTH": [
              "ene.",
              "feb.",
              "mar.",
              "abr.",
              "may.",
              "jun.",
              "jul.",
              "ago.",
              "sept.",
              "oct.",
              "nov.",
              "dic."
            ],
            "STANDALONEMONTH": [
              "Enero",
              "Febrero",
              "Marzo",
              "Abril",
              "Mayo",
              "Junio",
              "Julio",
              "Agosto",
              "Septiembre",
              "Octubre",
              "Noviembre",
              "Diciembre"
            ],
            "WEEKENDRANGE": [
              5,
              6
            ],
            "fullDate": "EEEE, d 'de' MMMM 'de' y",
            "longDate": "d 'de' MMMM 'de' y",
            "medium": "MM/dd/y h:mm:ss a",
            "mediumDate": "MM/dd/y",
            "mediumTime": "h:mm:ss a",
            "short": "MM/dd/yy h:mm a",
            "shortDate": "MM/dd/yy",
            "shortTime": "h:mm a"
          },
          "NUMBER_FORMATS": {
            "CURRENCY_SYM": "B/.",
            "DECIMAL_SEP": ".",
            "GROUP_SEP": ",",
            "PATTERNS": [
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 3,
                "minFrac": 0,
                "minInt": 1,
                "negPre": "-",
                "negSuf": "",
                "posPre": "",
                "posSuf": ""
              },
              {
                "gSize": 3,
                "lgSize": 3,
                "maxFrac": 2,
                "minFrac": 2,
                "minInt": 1,
                "negPre": "-\u00a4",
                "negSuf": "",
                "posPre": "\u00a4",
                "posSuf": ""
              }
            ]
          },
          "id": "es-pa",
          "pluralCat": function(n, opt_precision) {  if (n == 1) {    return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
        });
        }]);
        break;
}
