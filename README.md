# cs401-banking-system

## TODO NOW
- add register option to atmgui
- handle CREATECUSTOMER request
- handle LOGOUT request
- handle DEPOSIT/WITHDRAW/TRANSFER requests

## TODO LATER
- add transaction history option
- test customer/teller login
- test customer atm actions
- test teller atm actions
- test teller account management actions

## Initial program start
Server loads customers, accounts, and account histories from file.

## Customer login
1. Customer sends a login request using card number PIN to server.  
2. Server checks with HashMap if valid combo.  
	- if valid server opens bank gui for card number
	- else send error card number/pin

## Customer is logged in
1. Checking/Saving accounts shown with balance  
2. ATM service options available  
	- Send server request for ATM services to update account  
	- sends transaction object to store in account history
	
Account history option  
	- returns account transactions
