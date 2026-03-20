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

// Required to send request body
// false = only read response
// true  = send request body also
httpConnection.setDoOutput(true)


//------------------------------------------------------------
// Step 5: Prepare and send request body
//------------------------------------------------------------
def requestBody = "hello world"

// Open output stream
// Connection point of view, he sends output to Server
def outputStream = httpConnection.getOutputStream()

// Flow of request body:
// requestBody → getBytes() → byte[] → outputStream.write(...) → HTTP body

/*
requestBody (String)
      |
      | getBytes()
      v
byte[] (UTF-8)
      |
      | outputStream.write(...)
      v
HTTP request body sent
*/

// Convert request text to bytes
def requestBytes = requestBody.getBytes("UTF-8")

// Write bytes to output stream
outputStream.write(requestBytes)


//------------------------------------------------------------
// Same logic can also be written in one line
//------------------------------------------------------------
/*
Java / Groovy often prefers:

function(otherFunction())

instead of:

tmp = otherFunction()
function(tmp)

Example:

outputStream.write(requestBody.getBytes("UTF-8"))

Execution order is:
1. evaluate inside brackets first
2. requestBody.getBytes("UTF-8") returns byte[]
3. outputStream.write(byte[]) is called
*/


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
        "Response: " + 
        "${httpResponseCode} ${httpResponseMessage} | " +
        "Type=(${httpResponseContentType}) | " +
        "Length=${httpResponseContentLength} Bytes"
    )

	// Get response stream
	// Connection point of view, it receives input from Server
	def responseStream = httpConnection.inputStream

	// Convert to text
	def httpResponseBody = responseStream.getText("UTF-8")

	log.info "----- Response Body -----"
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