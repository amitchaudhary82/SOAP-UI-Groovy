//------------------------------------------------------------
// Step 1: Create URL object
// Host + path are taken from this URL
//------------------------------------------------------------
def httpUrl = new URL("https://httpbin.org/post")


//------------------------------------------------------------
// Step 2: Open generic connection
// At this stage it is only a general URLConnection
//------------------------------------------------------------
def urlConnection = httpUrl.openConnection()


//------------------------------------------------------------
// Step 3: Cast to HTTP connection
// Required to use HTTP-specific functions
//------------------------------------------------------------
def httpConnection = (HttpURLConnection) urlConnection


//------------------------------------------------------------
// Step 4: Set HTTP request
//------------------------------------------------------------
httpConnection.setRequestMethod("POST")

// Request headers
httpConnection.setRequestProperty("Content-Type", "text/plain")
httpConnection.setRequestProperty("Connection", "close")

// Required for POST body output
// false = only read response
// true  = send request body also
httpConnection.setDoOutput(true)


//------------------------------------------------------------
// Step 5: Prepare and send request body
//------------------------------------------------------------
def requestBody = "hello world"

// Open output channel to send request body
def outputStream = httpConnection.getOutputStream()

// Convert text to UTF-8 bytes and send them
outputStream.write(requestBody.getBytes("UTF-8"))

// Force any buffered bytes to be sent
outputStream.flush()

// Finished sending request body
outputStream.close()


//------------------------------------------------------------
// Step 6: Read response
//------------------------------------------------------------
try {

    def httpResponseCode = httpConnection.responseCode
    def httpResponseMessage = httpConnection.responseMessage
    def httpResponseContentType = httpConnection.contentType
    def httpResponseContentLength = httpConnection.contentLength

    log.info(
        "Response: ${httpResponseCode} ${httpResponseMessage} " +
        "(${httpResponseContentType}), ${httpResponseContentLength} Bytes"
    )

    log.info "----- Response Body -----"

    def httpResponseBody = httpConnection.inputStream.getText("UTF-8")
    log.info httpResponseBody
}


//------------------------------------------------------------
// Step 7: Exception handling
//------------------------------------------------------------
catch (Exception httpError) {

    log.info "Error: " + httpError
}


//------------------------------------------------------------
// Step 8: Disconnect connection
//------------------------------------------------------------
finally {

    httpConnection.disconnect()
}
