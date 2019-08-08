## Wee blockchain
#### Just experimenting :)
A simple blockchain that records transactions and allows new blocks to be mined using a basic proof of work algorithm. It's decentralized and can support multiple nodes using a consensus algorithm.

Fire up the server and then I used Postman to interact with the API over a network. Otherwise just use cURL. 

###### Register a new node
Make a POST request to http://localhost:1917/nodes/register 
with the body like `{
 "nodes": "http://localhost:1918"
}`

Then make a GET request to http://localhost:1917/nodes/resolve

###### Mine a block
Mine a block by making a GET request to http://localhost:1917/mine
The miner is granted 1 token.

###### Make a new transaction
Make a POST request to http://localhost:1917/transactions/new
with body `{
 "sender": "d4ee26eee15148ee92c6cd394edd974e",
 "recipient": "some-other-address",
 "amount": 16
}`

###### See the full blockchain
Make a GET request to http://localhost:1917/chain
