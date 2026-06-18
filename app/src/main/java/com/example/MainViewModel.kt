package com.example

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = AppRepository(database.appDao())

    // UI flows backed by SQLite Flow streams
    val allListings = repository.allListings
    val allLeads = repository.allLeads
    val allReviews = repository.allReviews
    val conversations = repository.conversations
    val notifications = repository.notifications
    val appSettings = repository.appSettings
    val cities = repository.cities
    val loginEvents = repository.loginEvents
    val profiles = repository.profiles
    val userRoles = repository.userRoles
    val bookings = repository.bookings
    val coupons = repository.coupons
    val dailyKpis = repository.dailyKpis

    // Interactive States
    private val _selectedCategory = MutableStateFlow("all")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _sortBy = MutableStateFlow("Best Match")
    val sortBy: StateFlow<String> = _sortBy

    // Search Autocomplete/Trending list
    val trendingSearches = listOf("PG near Pune University", "Homestyle Food Daily", "Wash & Iron express", "Silent study cabins", "Used engineering books")
    private val _recentSearches = MutableStateFlow(listOf("PG Shivaji Nagar", "Tiffin pure veg", "Dry cleaning model colony"))
    val recentSearches: StateFlow<List<String>> = _recentSearches

    // Filtering options
    private val _showFilterDialog = MutableStateFlow(false)
    val showFilterDialog: StateFlow<Boolean> = _showFilterDialog

    private val _filterPriceMax = MutableStateFlow(15000f)
    val filterPriceMax: StateFlow<Float> = _filterPriceMax

    private val _filterRatingMin = MutableStateFlow(0f)
    val filterRatingMin: StateFlow<Float> = _filterRatingMin

    private val _filterVerifiedOnly = MutableStateFlow(false)
    val filterVerifiedOnly: StateFlow<Boolean> = _filterVerifiedOnly

    private val _filterAC = MutableStateFlow(false)
    val filterAC: StateFlow<Boolean> = _filterAC

    private val _filterWiFi = MutableStateFlow(false)
    val filterWiFi: StateFlow<Boolean> = _filterWiFi

    // Selected listing for Details sheet
    private val _selectedListingForDetails = MutableStateFlow<Listing?>(null)
    val selectedListingForDetails: StateFlow<Listing?> = _selectedListingForDetails

    // Selected lead for CRM details pane
    private val _selectedLeadForCrm = MutableStateFlow<Lead?>(null)
    val selectedLeadForCrm: StateFlow<Lead?> = _selectedLeadForCrm

    val selectedLeadNotes = _selectedLeadForCrm.flatMapLatest { lead ->
        if (lead == null) flowOf(emptyList())
        else repository.getLeadNotes(lead.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedLeadEvents = _selectedLeadForCrm.flatMapLatest { lead ->
        if (lead == null) flowOf(emptyList())
        else repository.getLeadEvents(lead.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Chat flow
    private val _activeConvId = MutableStateFlow<String?>("conv_rohan")
    val activeConvId: StateFlow<String?> = _activeConvId

    val activeMessages = _activeConvId.flatMapLatest { convId ->
        if (convId == null) flowOf(emptyList())
        else repository.getMessages(convId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Dynamic Wizard States
    private val _wizardCategory = MutableStateFlow("accommodation")
    val wizardCategory: StateFlow<String> = _wizardCategory

    private val _wizardServiceType = MutableStateFlow("")
    val wizardServiceType: StateFlow<String> = _wizardServiceType

    private val _wizardSubcategory = MutableStateFlow("")
    val wizardSubcategory: StateFlow<String> = _wizardSubcategory

    private val _wizardStep = MutableStateFlow(1) // 1 category, 2 service type, 3 subcategory, 4+ dynamic workflow
    val wizardStep: StateFlow<Int> = _wizardStep

    // Temporary wizard inputs
    val wizardTitle = MutableStateFlow("")
    val wizardDesc = MutableStateFlow("")
    val wizardPrice = MutableStateFlow("")
    val wizardAddress = MutableStateFlow("")
    val wizardDistance = MutableStateFlow("")
    val wizardTags = MutableStateFlow("")
    val wizardIsAC = MutableStateFlow(false)
    val wizardIsWifi = MutableStateFlow(false)
    val wizardTiffinType = MutableStateFlow("Veg") // Veg, Non Veg, Jain, Vegan
    val wizardMealPlans = MutableStateFlow(setOf("Monthly")) // Daily, Weekly, Monthly
    val wizardMealsSelected = MutableStateFlow(setOf("Lunch", "Dinner")) // Breakfast, Lunch, Dinner
    val wizardLaundryGarments = MutableStateFlow(setOf("Shirt", "T-Shirt", "Jeans"))
    val wizardLaundryPricingModel = MutableStateFlow("Per KG") // Per KG, Per Item
    val wizardLibraryAmenities = MutableStateFlow(setOf("Silent Zone", "AC", "WiFi"))
    val wizardTotalBeds = MutableStateFlow("4")
    val wizardBedsAvailable = MutableStateFlow("2")

    // General app banners / notices
    private val _popupMessage = MutableStateFlow<String?>(null)
    val popupMessage: StateFlow<String?> = _popupMessage

    init {
        viewModelScope.launch {
            repository.seedIfNeeded()
        }
    }

    fun selectCategory(cat: String) {
        _selectedCategory.value = cat
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSortBy(sort: String) {
        _sortBy.value = sort
    }

    fun dismissFilterDialog() {
        _showFilterDialog.value = false
    }

    fun openFilterDialog() {
        _showFilterDialog.value = true
    }

    fun setFilterPriceMax(price: Float) {
        _filterPriceMax.value = price
    }

    fun setFilterRatingMin(rating: Float) {
        _filterRatingMin.value = rating
    }

    fun toggleFilterVerified(v: Boolean) {
        _filterVerifiedOnly.value = v
    }

    fun toggleFilterAC(v: Boolean) {
        _filterAC.value = v
    }

    fun toggleFilterWiFi(v: Boolean) {
        _filterWiFi.value = v
    }

    fun selectListingForDetails(listing: Listing?) {
        _selectedListingForDetails.value = listing
    }

    fun selectLeadForCrm(lead: Lead?) {
        _selectedLeadForCrm.value = lead
    }

    fun selectActiveConv(convId: String?) {
        _activeConvId.value = convId
    }

    fun addRecentSearch(search: String) {
        if (search.isNotBlank() && !_recentSearches.value.contains(search)) {
            _recentSearches.value = (listOf(search) + _recentSearches.value).take(5)
        }
    }

    fun clearRecentSearches() {
        _recentSearches.value = emptyList()
    }

    // Lead Actions (SaaS CRM operating system)
    fun updateLeadStatus(lead: Lead, newStatus: String) {
        viewModelScope.launch {
            val updated = lead.copy(status = newStatus, lastUpdated = "2026-06-18 10:45 AM")
            repository.updateLead(updated)
            _selectedLeadForCrm.value = updated

            // Add Event Log entry
            repository.insertLeadEvent(
                LeadEvent(
                    leadId = lead.id,
                    eventType = "StatusChanged",
                    timestamp = "Just Now",
                    details = "Lead status updated to '$newStatus'"
                )
            )

            // Trigger notification
            repository.addNotification(
                Notification(
                    title = "CRM Pipeline Update",
                    body = "${lead.tenantName} is now in status '$newStatus'",
                    timestamp = "Just Now",
                    isRead = false,
                    type = "NewBooking"
                )
            )
        }
    }

    fun addLeadNote(leadId: Int, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val newNote = LeadNote(
                leadId = leadId,
                noteText = text,
                authorName = "StayHub Host Manager",
                timestamp = "Just Now"
            )
            repository.insertLeadNote(newNote)

            // Also record event
            repository.insertLeadEvent(
                LeadEvent(
                    leadId = leadId,
                    eventType = "NoteAdded",
                    timestamp = "Just Now",
                    details = "Added context: '$text'"
                )
            )
        }
    }

    // Message sending with simulated quick responses from students!
    fun sendMessage(text: String, type: String = "Text", mediaUrl: String = "", voiceDur: Int = 0) {
        val convId = _activeConvId.value ?: return
        if (text.isBlank() && mediaUrl.isBlank()) return

        viewModelScope.launch {
            // Sender is Host (101)
            val msg = Message(
                conversationId = convId,
                senderId = 101,
                text = text,
                type = type,
                mediaUrl = mediaUrl,
                voiceDuration = voiceDur,
                timestamp = "08:34 AM"
            )
            repository.insertMessage(msg)

            // Simulate quick reactive reply from student after 1.5 seconds spacer!
            kotlinx.coroutines.delay(1200)

            val autoReplyText = when {
                text.lowercase().contains("coupon") || text.lowercase().contains("discount") ->
                    "Wow that's super helpful, I'll apply the code WELCOMECAMPUS and block my booking bed right now!"
                text.lowercase().contains("price") || text.lowercase().contains("rent") ->
                    "Got it, the monthly charges of ₹$text look perfect for my budget. Do you need business PAN or GSTR proofs?"
                else -> "Awesome, thank you for confirming standard laundry turnaround and support schedules, I'm ready to proceed!"
            }

            val studentMsg = Message(
                conversationId = convId,
                senderId = 1, // Student User
                text = autoReplyText,
                type = "Text",
                timestamp = "08:35 AM"
            )
            repository.insertMessage(studentMsg)

            // Notify
            repository.addNotification(
                Notification(
                    title = "New Incoming Inbox Message",
                    body = "Ananya Iyer: $autoReplyText",
                    timestamp = "Just Now",
                    isRead = false,
                    type = "NewOrder"
                )
            )
        }
    }

    // Tenant / Booking trigger
    fun submitBookingFlow(listing: Listing, roomType: String = "Single Room", pricePaid: Double = 7500.0) {
        viewModelScope.launch {
            repository.insertBooking(
                Booking(
                    listingId = listing.id,
                    tenantId = 1, // Student
                    checkIn = "2026-07-01",
                    checkOut = "2027-05-30",
                    amountPaid = pricePaid,
                    status = "Confirmed",
                    bookingDate = "2026-06-18"
                )
            )

            // Create lead for host CRM too
            repository.insertLead(
                Lead(
                    listingId = listing.id,
                    tenantName = "Ananya Iyer",
                    tenantPhone = "+91 91234 56789",
                    status = "Converted",
                    lastUpdated = "2026-06-18 12:00 PM",
                    score = 100,
                    source = "Direct Booking Flow"
                )
            )

            _popupMessage.value = "Success! Premium Booking processed securely. Your bed is confirmed."

            repository.addNotification(
                Notification(
                    title = "New Booking Confirmed!",
                    body = "Received ₹$pricePaid seat deposit for ${listing.title}",
                    timestamp = "Just Now",
                    isRead = false,
                    type = "PaymentReceived"
                )
            )
        }
    }

    fun clearPopupMessage() {
        _popupMessage.value = null
    }

    // Dynamic Multi-Step Wizard Configuration
    val stepsForSelectedCategory: StateFlow<List<String>> = combine(
        _wizardCategory, _wizardSubcategory
    ) { category, _ ->
        val generatedSteps = when (category) {
            "accommodation" -> listOf("Basics", "Location", "Room Config", "Pricing", "Amenities", "Photos", "Availability", "Policies", "SEO Summary")
            "food" -> listOf("Basics", "Food Type & Cuisine", "Menu List", "Meal Plans Selection", "Subscription Setup", "Delivery Area", "Promo Media", "Policy Rules")
            "laundry" -> listOf("Basics", "Service Care Type", "Pricing Matrix", "Pickup Windows", "Delivery Area", "Turnaround Duration", "Coverage", "Visual Media", "House Rules")
            "study" -> listOf("Basics", "Space Category", "Seating Matrix", "Membership Plans", "Open Timings", "Premium Amenities", "Media Uploads", "Policies")
            "books" -> listOf("Basics", "Condition Setup", "Pricing Model", "Delivery Method", "High-res Cover", "Exchange Policies")
            else -> listOf("Basics", "Config Setup", "Review & Launch")
        }
        listOf("Select Category", "Select Service Type", "Select Subcategory") + generatedSteps
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setupWizardIntro(category: String, serviceType: String, subcat: String) {
        _wizardCategory.value = category
        _wizardServiceType.value = serviceType
        _wizardSubcategory.value = subcat
        _wizardStep.value = 1 // reset to first custom page
    }

    fun updateWizardTaxonomy(category: String, serviceType: String, subcat: String) {
        _wizardCategory.value = category
        _wizardServiceType.value = serviceType
        _wizardSubcategory.value = subcat
    }

    fun nextWizardStep() {
        val total = stepsForSelectedCategory.value.size
        if (_wizardStep.value < total) {
            _wizardStep.value = _wizardStep.value + 1
        }
    }

    fun prevWizardStep() {
        if (_wizardStep.value > 1) {
            _wizardStep.value = _wizardStep.value - 1
        }
    }

    fun publishWizardListing() {
        viewModelScope.launch {
            val extraConfigString = when (_wizardCategory.value) {
                "accommodation" -> """{"amenities":[${if (wizardIsWifi.value) "\"WiFi\"" else ""}${if (wizardIsWifi.value && wizardIsAC.value) "," else ""}${if (wizardIsAC.value) "\"AC\"" else ""}], "totalBeds":${wizardTotalBeds.value.toIntOrNull() ?: 4}, "availableBeds":${wizardBedsAvailable.value.toIntOrNull() ?: 2}}"""
                "food" -> """{"foodType":"${wizardTiffinType.value}","mealPlans":[${wizardMealPlans.value.joinToString(",") { "\"$it\"" }}],"menu":"${wizardDesc.value}"}"""
                "laundry" -> """{"pricingModels":["${wizardLaundryPricingModel.value}"],"turnaroundTime":"24 hours","expressService":true,"garments":[${wizardLaundryGarments.value.joinToString(",") { "\"$it\"" }}]}"""
                "study" -> """{"amenities":[${wizardLibraryAmenities.value.joinToString(",") { "\"$it\"" }}],"timings":"24/7 Access"}"""
                else -> """{"condition":"Excellent"}"""
            }

            val priceVal = wizardPrice.value.toDoubleOrNull() ?: 1200.0

            val listing = Listing(
                ownerId = 101, // Managed locally
                title = if (wizardTitle.value.isNotBlank()) wizardTitle.value else "Premium Service Lounge",
                description = if (wizardDesc.value.isNotBlank()) wizardDesc.value else "Fully compliant and verified Premium student services.",
                categoryId = _wizardCategory.value,
                subcategoryId = _wizardSubcategory.value,
                serviceTypeId = _wizardServiceType.value,
                price = priceVal,
                rating = 5.0,
                reviewCount = 0,
                verifiedBadge = true,
                availabilityStatus = "Immediate Start",
                distance = if (wizardDistance.value.isNotBlank()) wizardDistance.value else "0.8 km from Campus",
                thumbUrl = when (_wizardCategory.value) {
                    "accommodation" -> "hostel_boys"
                    "food" -> "tiffin"
                    "laundry" -> "laundry"
                    "study" -> "library"
                    else -> "books"
                },
                address = if (wizardAddress.value.isNotBlank()) wizardAddress.value else "Main University Galleria, Pune",
                tags = if (wizardTags.value.isNotBlank()) wizardTags.value else "Campus Partner, Student Discounted",
                extraConfigJson = extraConfigString
            )

            val lid = repository.insertListing(listing).toInt()

            if (_wizardCategory.value == "accommodation") {
                // Add room beds configuration records
                repository.insertRoom(
                    StayRoom(
                        listingId = lid,
                        roomType = "Premium Bed Setup",
                        totalBeds = wizardTotalBeds.value.toIntOrNull() ?: 4,
                        availableBeds = wizardBedsAvailable.value.toIntOrNull() ?: 2,
                        monthlyPrice = priceVal
                    )
                )
            }

            // Create notification
            repository.addNotification(
                Notification(
                    title = "New Category Listing Published!",
                    body = "'${listing.title}' is now live on StayHub campus marketplace.",
                    timestamp = "Just Now",
                    isRead = false,
                    type = "NewLead"
                )
            )

            // Reset Wizard Inputs
            wizardTitle.value = ""
            wizardDesc.value = ""
            wizardPrice.value = ""
            wizardAddress.value = ""
            wizardDistance.value = ""
            wizardTags.value = ""
            _wizardStep.value = 1

            _popupMessage.value = "Hooray! Your brand new listing is published live and seaded successfully."
        }
    }

    // Reset standard Seed data trigger
    fun resetDatabaseSeed() {
        viewModelScope.launch {
            // Destructive reset
            database.clearAllTables()
            repository.seedIfNeeded()
            _popupMessage.value = "All 39 Database tables cleared and preseeded successfully to compliant specs!"
        }
    }

    // Submit promotional coupon
    fun addPromoCoupon(code: String, disc: Double, type: String) {
        if (code.isBlank() || disc <= 0) return
        viewModelScope.launch {
            repository.insertCoupon(
                Coupon(code = code, discountAmount = disc, type = type, maxUses = 100, active = true)
            )
            _popupMessage.value = "Campaign Coupon '$code' launched securely."
        }
    }
}
