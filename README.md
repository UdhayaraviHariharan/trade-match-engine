# trade-match-engine

Edge Cases
1. System shutdown
THe orders ideally will be persisted to a DB. So they are not lost during shut down

2. Orders 
* Missing values in order like quantity , price cannot occur because no default initialization / any other initialization without the mandatory values
* price/quantity cannot be null, 0 or negative
* Orders cannot be executed without instrument
* Orders cannot be traded for an incorrect insturment as instrumentId is part of the orders
* Quantity is set to long to support large order
* Order overflow will not happen as we have set a limit in the trading system 
* Duplicate order ids cannot happen as the system creates the id


3. Performance improvement considerations (ToDo)
* Add some locks and unlocks on the trade
* Can think about adding async to the add orders and execute trade processor
* Use caching perhaps ? maybe for market price
* Think about adding profilers to identify performance bottlenecks
