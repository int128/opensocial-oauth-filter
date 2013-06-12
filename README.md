OpenSocial OAuth Filter
=======================

OpenSocial OAuth Filter is a JavaEE filter to validate OpenSocial requests.

This filter allows only valid requests and prevents spoofing and tampering. Signature of request is validated as follows:

 1. The OpenSocial gadget sends a request via OpenSocial server, using gadget.io.makeRequest() method.
 2. OpenSocial OAuth Filter receives the request and validates its signature.
 3. If validation passed, the request will be sent to your servlet. Otherwise, the request will be blocked.

For details, please visit https://code.google.com/p/opensocial-oauth-filter/
