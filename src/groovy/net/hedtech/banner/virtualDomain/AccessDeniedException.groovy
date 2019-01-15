package net.hedtech.restfulapi

class AccessDeniedException extends RuntimeException {

    private int statusCode
    private def errorMessage
    private def errorType
    private List messageArgs = new ArrayList()

    AccessDeniedException(def msg, messageArgs) {
        this.statusCode = 403
        this.errorType = "access"
        this.errorMessage = msg
        this.messageArgs = messageArgs

    }

    public def getHttpStatusCode() {
        return statusCode
    }


    public returnMap = { localize ->
        def map = [:]
        if (errorMessage) {
            map.message = translate(localize: localize,
                    code: errorMessage,
                    args: messageArgs.collect {
                        localize( code: it ) }) as String
        }
        if (errorType) {
            map.errors = [type: errorType, errorMessage: map.message.replace("\n", " ")]
        }
        if (errorType == "access") {
            map.headers = ['X-Status-Reason': 'Access Denied']
        }
        map
    }

    private String translate(map) {
        def msg = map.localize(code: "${map.code}", args: map.args)
        if (msg == "${map.code}") {
            if (errorMessage) {
                msg = map.localize(code: "default.${map.code}", args: map.args, default: errorMessage)
            } else {
                msg = map.localize(code: "default.${map.code}", args: map.args)
            }
        }
        msg
    }
}

