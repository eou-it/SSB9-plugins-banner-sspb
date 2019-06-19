// #################################################################################################################
// Name: Logging
// Type: Neccessary setup configs for setting up log4javascript.
// #################################################################################################################

// Setup the default logging.
// TODO:  Logging appenders and layout formats should come from the server via the HTML and used to dynmically setup logging.
var log = log4javascript.getLogger();

var level = log4javascript.Level.DEBUG;  // Default value

if ($("meta[name='logLevel']")) {
    switch($("meta[name='logLevel']").attr('content')) {
        case "OFF":
            level = log4javascript.Level.OFF;
            break;
        case "FATAL":
            level = log4javascript.Level.FATAL;
            break;
        case "ERROR":
            level = log4javascript.Level.ERROR;
            break;
        case "WARN":
            level = log4javascript.Level.WARN;
            break;
        case "INFO":
            level = log4javascript.Level.INFO;
            break;
        case "DEBUG":
            level = log4javascript.Level.DEBUG;
            break;
        case "TRACE":
            level = log4javascript.Level.TRACE;
            break;
        case "ALL":
            level = log4javascript.Level.ALL;
            break;
    }
}

log.setLevel( level );

var browserConsoleAppender = new log4javascript.BrowserConsoleAppender();
var browserConsoleUpLayout = new log4javascript.PatternLayout("%d{HH:mm:ss} %-5p - %m%n");
browserConsoleAppender.setLayout( browserConsoleUpLayout );
log.addAppender(browserConsoleAppender);