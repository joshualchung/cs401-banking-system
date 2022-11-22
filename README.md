# cs401-banking-system

## TODO NOW
- connect login gui to client
- add Register class and add option to client

## TODO LATER
- add bank teller logic (file storage, login, teller class)
- add transaction class and transaction history feature

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


### Customer class
first name
last name
card number (9 digits)
pin
accounts (checking/saving)

### Account class
number	(9 digits)  
balance  
history of transactions  

### Transaction
date  
action  
current account  
target  

### Register class
first name  
last name  
generated account numbers (UNIQUE ID UUID)  
pin  