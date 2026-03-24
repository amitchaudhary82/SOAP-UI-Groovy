//------------------------------------------------------------
// Step 1: Create URL object
//------------------------------------------------------------
def httpUrl = new URL("https://httpbin.org/ip")


//------------------------------------------------------------
// Step 2: Open generic connection
// Could be HTTP / HTTPS / FTP / FILE / JAR etc.
//------------------------------------------------------------
def urlConnection = httpUrl.openConnection()


//------------------------------------------------------------
// Step 3: Cast to HttpURLConnection
// Required to use HTTP specific functions
//------------------------------------------------------------
def httpConnection = (HttpURLConnection) urlConnection


//------------------------------------------------------------
// Step 4: Set HTTP request properties
//------------------------------------------------------------
httpConnection.setRequestMethod("GET")
httpConnection.setRequestProperty("Connection", "close")
def requestCount = 0



//------------------------------------------------------------
// Print request headers BEFORE call
//------------------------------------------------------------
log.info "----- Request Headers -----"
httpConnection.getRequestProperties().each { name, value ->

	// value is List<String>
	// Example: [A, B, C] → join(", ") → "A, B, C"
	// HTTP example: [text/html, application/json] → "text/html, application/json"on"
    def headerVal = value.join(", ")

    log.info "Header      : ${name} = ${headerVal}"
}



//------------------------------------------------------------
// Step 5: Send request and read response
//------------------------------------------------------------
try {

	requestCount = requestCount + 1
	def httpResponseCode = httpConnection.responseCode
	def httpResponseMessage = httpConnection.responseMessage
	def httpResponseContentType = httpConnection.contentType
	def httpResponseContentLength = httpConnection.contentLength
	
	log.info(
        "Response: " + 
        "${httpResponseCode} ${httpResponseMessage} | " +
        "Type=(${httpResponseContentType}) | " +
        "Length=${httpResponseContentLength} Bytes"
    )

    //--------------------------------------------------------
    // Print all response headers
    //--------------------------------------------------------
    log.info "----- Response Headers -----"

    httpConnection.headerFields.each { name, value ->

		if (name == null) {
		log.info "Status Line : ${value}"
   		 }
    		else {
        		log.info "Header      : ${name} = ${value}"
    		}
	}


	//--------------------------------------------------------
    	// Get response body
    	//--------------------------------------------------------
	// Connection point of view, it receives input from Server
	def responseStream = httpConnection.inputStream

	// Convert to text
	def httpResponseBody = responseStream.getText("UTF-8")

	log.info "----- Response Body -----"
	log.info httpResponseBody
    
}


//------------------------------------------------------------
// Step 6: Exception Handling
//------------------------------------------------------------
catch (Exception httpError) {

    log.info "Error: " + httpError
}


//------------------------------------------------------------
// Step 7: Disconnect connection
//------------------------------------------------------------
finally {

    httpConnection.disconnect()
}



// Need to Refactor below
//--------------------------------------------------------
// Extra debug for error responses
//--------------------------------------------------------

if (httpResponseCode >= 400) {

    log.info "----- Error Debug -----"

    log.info "URL    : ${httpUrl}"
    log.info "Method : GET"

    def authHeader = httpConnection.getHeaderField("WWW-Authenticate")

    if (authHeader != null) {
        log.info "WWW-Authenticate : ${authHeader}"
    }

    def errorStream = httpConnection.getErrorStream()

    if (errorStream != null) {

        def errorBody = errorStream.getText("UTF-8")

        log.info "----- Error Body -----"
        log.info errorBody
    }
}



if (httpResponseCode >= 400) {

    def errorStream = httpConnection.getErrorStream()

    if (errorStream != null) {

        def errorBody = errorStream.getText("UTF-8")

        log.info "----- Error Body -----"
        log.info errorBody
    }
}




//------------------------------------------------------------
// Common function : print request info + timing
//------------------------------------------------------------
def printRequestInfo(httpUrl, httpMethod, startTime) {

    log.info "----- URL Info -----"

    log.info "Protocol : ${httpUrl.getProtocol()}"
    log.info "Host     : ${httpUrl.getHost()}"
    log.info "Port     : ${httpUrl.getPort()}"
    log.info "Path     : ${httpUrl.getPath()}"


    log.info "----- Request Line -----"

    log.info "${httpMethod} ${httpUrl.getPath()} HTTP/1.1"
    log.info "Host: ${httpUrl.getHost()}"


    if (startTime != null) {

        def endTime = System.currentTimeMillis()
        def duration = endTime - startTime

        log.info "Time : ${duration} ms"
    }
}



def startTime = System.currentTimeMillis()


def startTime = System.currentTimeMillis()
printRequestInfo(httpUrl, "GET", startTime)


def startTime = System.currentTimeMillis()

def httpResponseCode = httpConnection.responseCode

printRequestInfo(httpUrl, "GET", startTime)


printRequestInfo(httpUrl, "GET", startTime)



// Fully Example Code - complete set - neet to factor in (use beyond compare
//------------------------------------------------------------
// Common function : print URL info + request line + timing
//------------------------------------------------------------
def printBasicRequestInfo(httpUrl, httpMethod, startTime) {

    log.info "----- URL Info -----"
    log.info "Protocol : ${httpUrl.getProtocol()}"
    log.info "Host     : ${httpUrl.getHost()}"
    log.info "Port     : ${httpUrl.getPort()}"
    log.info "Path     : ${httpUrl.getPath()}"

    log.info "----- Request Line -----"
    log.info "${httpMethod} ${httpUrl.getPath()} HTTP/1.1"
    log.info "Host: ${httpUrl.getHost()}"

    def endTime = System.currentTimeMillis()
    def duration = endTime - startTime
    log.info "Time : ${duration} ms"
}


//------------------------------------------------------------
// Common function : print headers
//------------------------------------------------------------
def printHeaders(headerTitle, headerMap) {

    log.info "----- ${headerTitle} -----"

    headerMap.each { name, value ->

        // value is List<String>
        // Example: [A, B, C] → join(", ") → "A, B, C"
        // HTTP example: [text/html, application/json] → "text/html, application/json"
        def headerVal = value.join(", ")

        if (name == null) {
            log.info "Status Line : ${headerVal}"
        }
        else {
            log.info "Header      : ${name} = ${headerVal}"
        }
    }
}


//------------------------------------------------------------
// Step 1: Create URL object
//------------------------------------------------------------
def httpUrl = new URL("https://httpbin.org/ip")
def httpMethod = "GET"


//------------------------------------------------------------
// Step 2: Open generic connection
// Could be HTTP / HTTPS / FTP / FILE / JAR etc.
//------------------------------------------------------------
def urlConnection = httpUrl.openConnection()


//------------------------------------------------------------
// Step 3: Cast to HttpURLConnection
// Required to use HTTP specific functions
//------------------------------------------------------------
def httpConnection = (HttpURLConnection) urlConnection


//------------------------------------------------------------
// Step 4: Set HTTP request properties
//------------------------------------------------------------
httpConnection.setRequestMethod(httpMethod)
httpConnection.setRequestProperty("Connection", "close")

def requestCount = 0


//------------------------------------------------------------
// Print request headers BEFORE call
//------------------------------------------------------------
printHeaders(
    "Request Headers",
    httpConnection.getRequestProperties()
)


//------------------------------------------------------------
// Step 5: Send request and read response
//------------------------------------------------------------
try {

    requestCount = requestCount + 1

    def startTime = System.currentTimeMillis()

    def httpResponseCode = httpConnection.responseCode
    def httpResponseMessage = httpConnection.responseMessage
    def httpResponseContentType = httpConnection.contentType
    def httpResponseContentLength = httpConnection.contentLength

    log.info(
        "[${requestCount}] " +
        "Response: ${httpResponseCode} ${httpResponseMessage} | " +
        "Type=(${httpResponseContentType}) | " +
        "Length=${httpResponseContentLength} Bytes"
    )

    //--------------------------------------------------------
    // Print basic request info + timing
    //--------------------------------------------------------
    printBasicRequestInfo(httpUrl, httpMethod, startTime)


    //--------------------------------------------------------
    // Print all response headers
    //--------------------------------------------------------
    printHeaders(
        "Response Headers",
        httpConnection.headerFields
    )


    //--------------------------------------------------------
    // Extra debug for error responses
    //--------------------------------------------------------
    if (httpResponseCode >= 400) {

        log.info "----- Error Debug -----"

        def authHeader = httpConnection.getHeaderField("WWW-Authenticate")
        if (authHeader != null) {
            log.info "WWW-Authenticate : ${authHeader}"
        }

        // inputStream = server → client (read response body)
        def errorStream = httpConnection.getErrorStream()

        if (errorStream != null) {
            def httpErrorBody = errorStream.getText("UTF-8")

            log.info "----- Error Body -----"
            log.info httpErrorBody
        }
        else {
            log.info "----- Error Body -----"
            log.info "No error body returned by server."
        }
    }
    else {

        //----------------------------------------------------
        // Get normal response body
        //----------------------------------------------------
        // Connection point of view, it receives input from Server
        def responseStream = httpConnection.inputStream

        // Convert to text
        def httpResponseBody = responseStream.getText("UTF-8")

        log.info "----- Response Body -----"
        log.info httpResponseBody
    }
}


//------------------------------------------------------------
// Step 6: Exception Handling
//------------------------------------------------------------
catch (Exception httpError) {

    log.info "----- Exception -----"
    log.info "Error Type    : ${httpError.getClass().getName()}"
    log.info "Error Message : ${httpError.getMessage()}"

    // Try to print error stream also, if available
    try {
        def errorStream = httpConnection.getErrorStream()

        if (errorStream != null) {
            def exceptionErrorBody = errorStream.getText("UTF-8")

            log.info "----- Exception Error Body -----"
            log.info exceptionErrorBody
        }
    }
    catch (Exception nestedError) {
        log.info "Could not read error stream: ${nestedError.getMessage()}"
    }
}


//------------------------------------------------------------
// Step 7: Disconnect connection
//------------------------------------------------------------
finally {

    httpConnection.disconnect()
}






// you can run through a file
/Users/amitchaudhary/soapui-scripts/http_debug.groovy


code in file
// http_debug.groovy

log.info "Running external script"

def httpUrl = new URL("https://httpbin.org/ip")

def conn = (HttpURLConnection) httpUrl.openConnection()

conn.setRequestMethod("GET")

log.info conn.responseCode

conn.disconnect()



in SOAP UI, write
def scriptFile = new File("/Users/amitchaudhary/soapui-scripts/http_debug.groovy")

def scriptText = scriptFile.text

evaluate(scriptText)


def scriptPath = "/Users/amitchaudhary/soapui-scripts/http_debug.groovy"

log.info "Running script from: " + scriptPath

def scriptFile = new File(scriptPath)

if (!scriptFile.exists()) {
    log.info "Script file not found"
    return
}

def scriptText = scriptFile.text

evaluate(scriptText)






// New 
We can make a framework style loader like:

runScript("http_get.groovy")
runScript("http_post.groovy")
runScript("http_auth.groovy")


We can make a framework style loader like:

runScript("http_get.groovy")
runScript("http_post.groovy")
runScript("http_auth.groovy")




//------------------------------------------------------------
// SoapUI launcher : base folder + reusable function
//------------------------------------------------------------
def scriptBasePath = "/Users/amitchaudhary/git/my-repo/soapui"


def runScript(scriptName) {

    def scriptPath = scriptBasePath + "/" + scriptName

    log.info "Running script from file: ${scriptPath}"

    def scriptFile = new File(scriptPath)

    if (!scriptFile.exists()) {
        log.info "ERROR: Script file not found: ${scriptPath}"
        return
    }

    def scriptText = scriptFile.getText("UTF-8")
    evaluate(scriptText)
}





//------------------------------------------------------------
// Call one script
//------------------------------------------------------------
runScript("015_get_httpbin_ip_200_telnet.groovy")







my-repo/
└── soapui/
    ├── 000_httpConnectionInfo.txt
    ├── 014_get_httpbin_get_200_telnet.groovy
    ├── 015_get_httpbin_ip_200_telnet.groovy
    ├── 016_post_httpbin_post_200_telnet.groovy
    └── 099_common_loader_notes.txt

def scriptBasePath = "/Users/amitchaudhary/git/my-repo/soapui"

def runScript(scriptName) {

    def scriptPath = scriptBasePath + "/" + scriptName
    def scriptFile = new File(scriptPath)

    log.info "Running script from file: ${scriptPath}"

    if (!scriptFile.exists()) {
        log.info "ERROR: Script file not found: ${scriptPath}"
        return
    }

    def binding = new Binding()
    binding.setProperty("log", log)
    binding.setProperty("context", context)
    binding.setProperty("testRunner", testRunner)

    def shell = new GroovyShell(binding)
    shell.evaluate(scriptFile.getText("UTF-8"))
}

runScript("015_get_httpbin_ip_200_telnet.groovy")




// http url functions
| Function         | Description (1 line)                      | Example Data                                     |
| ---------------- | ----------------------------------------- | ------------------------------------------------ |
| getProtocol()    | Returns protocol of URL                   | https                                            |
| getHost()        | Returns hostname                          | httpbin.org                                      |
| getPort()        | Returns port number, -1 if default        | -1                                               |
| getDefaultPort() | Returns default port for protocol         | 443                                              |
| getPath()        | Returns path part of URL                  | /ip                                              |
| getFile()        | Returns path + query                      | /api/test?id=10                                  |
| getQuery()       | Returns query string only                 | id=10&name=test                                  |
| getAuthority()   | Returns host + port                       | httpbin.org:8080                                 |
| getUserInfo()    | Returns username:password from URL        | user:pass                                        |
| getRef()         | Returns fragment after #                  | part1                                            |
| toString()       | Returns full URL as string                | [https://httpbin.org/ip](https://httpbin.org/ip) |
| toURI()          | Converts URL to URI object                | [https://httpbin.org/ip](https://httpbin.org/ip) |
| openConnection() | Opens connection to URL                   | HttpURLConnection                                |
| openStream()     | Opens input stream directly (GET)         | InputStream                                      |
| getContent()     | Returns content object from URL           | HTML / JSON                                      |
| sameFile(URL)    | Checks if two URLs refer to same resource | true                                             |
| toExternalForm() | Returns full URL string                   | [https://httpbin.org/ip](https://httpbin.org/ip) |




    
