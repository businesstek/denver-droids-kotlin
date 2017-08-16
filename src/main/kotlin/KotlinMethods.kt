/**
 * SECTION 1
 *
 * Elvis Operator and Nullability
 */

class Client(val personalInfo: PersonalInfo?)

class PersonalInfo(val email: String?)

interface Mailer {
    fun sendMessage(email: String, message: String)
}

// Rewrite Java method 'SampleJavaMethods.sendMessageToClient' in Kotlin
fun sendMessageToClient(client: Client?, message: String?, mailer: Mailer) {
    if (client == null || message == null) return

    val personalInfo = client.personalInfo ?: return
    val email = personalInfo.email ?: return

    mailer.sendMessage(email, message)
}


/**
 * SECTION 2
 *
 * Let's Get Functional
 */

data class Shop(val name: String, val customers: List<Customer>)

data class Customer(val name: String, val city: City, val orders: List<Order>)

data class Order(val products: List<Product>, val isDelivered: Boolean)

data class Product(val name: String, val price: Double)

data class City(val name: String)

// Return a set containing all the customers of this shop
// Hint: List 'to' a Set
fun Shop.getSetOfCustomers(): Set<Customer> {
    return this.customers.toSet()
}

// Return the set of cities the customers are from
// Hint: Map
fun Shop.getCitiesCustomersAreFrom(): Set<City> {
    return this.customers.map { it.city }.toSet()
}

// Return a list of the customers who live in the given city
// Hint: Filter
fun Shop.getCustomersFrom(city: City): List<Customer> {
    return this.customers.filter { it.city == city }
}

// Return true if the customer is from the given city
// Hint: Predicates
fun Customer.isFrom(city: City): Boolean {
    return this.city == city
}

// Return true if all customers are from the given city
// Hint: Predicates
fun Shop.checkAllCustomersAreFrom(city: City): Boolean {
    return this.customers.all { it.isFrom(city) }
}

// Return true if there is at least one customer from the given city
// Hint: Predicates
fun Shop.hasCustomerFrom(city: City): Boolean {
    return this.customers.any { it.isFrom(city) }
}

// Return the number of customers from the given city
// Hint: Predicates
fun Shop.countCustomersFrom(city: City): Int {
    return this.customers.count { it.isFrom(city) }
}

// Return a customer who lives in the given city, or null if there is none
// Hint: Predicates
fun Shop.findAnyCustomerFrom(city: City): Customer? {
    return this.customers.first { it.isFrom(city) }
}

// Return all products that were ordered by customer
// Hint: Flat map
val Customer.orderedProducts: Set<Product> get() {
    return this.orders.flatMap { it.products }.toSet()
}

// Return all products that were ordered by at least one customer
// Hint: Flat map
val Shop.allOrderedProducts: Set<Product> get() {
    return this.customers.flatMap { it.orderedProducts }.toSet()
}

// Return a customer whose order count is the highest among all customers
// Hint: Max/Min
fun Shop.getCustomerWithMaximumNumberOfOrders(): Customer? {
    return this.customers.maxBy { it.orders.size }
}

// Return the most expensive product which has been ordered
// Hint: Max/Min
fun Customer.getMostExpensiveOrderedProduct(): Product? {
    return this.orderedProducts.maxBy { it.price }
}

// Return a list of customers, sorted by the ascending number of orders they made
// Hint: Sort
fun Shop.getCustomersSortedByNumberOfOrders(): List<Customer> {
    return this.customers.sortedBy { it.orders.size }
}

// Return the sum of prices of all products that a customer has ordered.
// Note: a customer may order the same product for several times.
// Hint: Sum
fun Customer.getTotalOrderPrice(): Double {
    return this.orderedProducts.sumByDouble { it.price }
}

// Find the correspondence between customers and their names (as a list or pairs)
// Hint: Zip
fun Shop.zipNameAndCustomer(): List<Pair<String, Customer>> {
    return customers.map { it.name }.zip(customers)
}

// Find the correspondence between customers and their names (as a map)
// Hint: AssociateBy
fun Shop.associateCustomersByName(): Map<String, Customer> {
    return this.customers.associateBy { it.name }
}

// Return a map of the customers living in each city
// Hint: GroupBy
fun Shop.groupCustomersByCity(): Map<City, List<Customer>> {
    return this.customers.groupBy { it.city }
}

// Return customers who have more undelivered orders than delivered
// Hint: Partition
fun Shop.getCustomersWithMoreUndeliveredOrdersThanDelivered(): Set<Customer> {
    val customerSet = HashSet<Customer>()
    for (customer in customers) {
        val orderPairs = customer.orders.partition { it.isDelivered }
        if (orderPairs.first.size < orderPairs.second.size) {
            customerSet.add(customer)
        }
    }
    return customerSet
}

// Return the set of products ordered by every customer
// Hint: Fold
fun Shop.getSetOfProductsOrderedByEveryCustomer(): Set<Product> {
    return customers.fold(allOrderedProducts, {
        orderedByAll, customer ->
        orderedByAll.intersect(customer.orderedProducts)
    })
}

// Return the most expensive product among all delivered products
// (use the Order.isDelivered flag)
// Hint: CompoundTasks
fun Customer.getMostExpensiveDeliveredProduct(): Product? {
    TODO()
}

// Return the number of times the given product was ordered.
// Note: a customer may order the same product for several times.
// Hint: CompoundTasks
fun Shop.getNumberOfTimesProductWasOrdered(product: Product): Int {
    TODO()
}

// Rewrite SampleJavaMethods.doSomethingStrangeWithCollection function to Kotlin.
fun doSomethingStrangeWithCollection(collection: Collection<String>): Collection<String>? {

    val groupsByLength = collection.groupBy { s -> TODO() }

    return groupsByLength.values.maxBy { group -> TODO() }
}

/**
 * SECTION 3
 *
 * No More Hints!
 *
 * Hint: Don't confuse Order from above with TaxiOrder here
 *
 * That's your last hint!
 */
class TaxiPark(
        val allDrivers: List<Driver>,
        val allPassengers: List<Passenger>,
        val orders: List<TaxiOrder>)

data class Driver(val name: String)
data class Passenger(val name: String)

data class TaxiOrder(
        val driver: Driver,
        val passengers: Collection<Passenger>,
        val duration: Int,
        val distance: Double,
        val discount: Double? = null
) {
    val cost: Double
        get() = (1 - (discount ?: 0.0)) * (duration + distance)
}

// Find all the drivers that completed no orders
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        TODO()

// Find all passengers having more than 'minTrips' number of trips
fun TaxiPark.findFaithfulPassengers(minTrips: Int): List<Passenger> =
        TODO()

// Find all passengers that were taken by this driver more than once
fun TaxiPark.findFrequentPassengers(driver: Driver): List<Passenger> =
        TODO()

// Find passengers that used discounts for majority of the trips
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        TODO()

// Find the most frequent trip interval duration (or several if there are many)
// among 0-9 minutes, 10-19 minutes, 20-29 minutes etc.
// If there's no duration info, return empty list.
fun TaxiPark.findTheMostFrequentTripDurations(): List<IntRange> {
    TODO()
}

// Check whether 20% of the drivers make 80% of the profit
fun TaxiPark.checkParetoPrinciple(): Boolean {
    TODO()
}

/**
 * Section 4
 *
 * There is no Section 4.
 *
 * If you want to continue, clone this git repo and follow the lessons here "student/advanced/src/main..."
 *
 * https://github.com/JetBrains/kotlin-workshop
 */