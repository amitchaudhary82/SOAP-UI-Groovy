//------------------------------------------------------------
// Step 1: Create URL object
//------------------------------------------------------------
def httpUrl = new URL("https://httpbin.org/")


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
// Step 5: Send request and read response
//------------------------------------------------------------
try {

	requestCount = requestCount + 1
	def httpResponseCode = httpConnection.responseCode
	def httpResponseMessage = httpConnection.responseMessage
	def httpResponseContentType = httpConnection.contentType
	def httpResponseContentLength = httpConnection.contentLength
	
	// old style
	/*
	log.info(
    		"[" + requestCount + "] : Response: " +
    		httpResponseCode + " " + httpResponseMessage +
    		" (" + httpResponseContentType + ")" +
    		", " + httpResponseContentLength + " Bytes")
	*/

	// new style using ${expression}
	log.info(
 		"[${requestCount}] : Response: " +
    		"${httpResponseCode} ${httpResponseMessage} " +
    		"(${httpResponseContentType}), " +
    		"${httpResponseContentLength} Bytes")        
        								

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
