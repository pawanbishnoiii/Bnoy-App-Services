package com.example.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppRepository(private val appDao: AppDao) {

    val allListings: Flow<List<Listing>> = appDao.getAllListings()
    val allLeads: Flow<List<Lead>> = appDao.getAllLeads()
    val allReviews: Flow<List<Review>> = appDao.getAllReviews()
    val conversations: Flow<List<Conversation>> = appDao.getConversations()
    val notifications: Flow<List<Notification>> = appDao.getAllNotifications()
    val appSettings: Flow<AppSettings?> = appDao.getAppSettings()
    val cities: Flow<List<City>> = appDao.getCities()
    val loginEvents: Flow<List<LoginEvent>> = appDao.getLoginEvents()
    val profiles: Flow<List<Profile>> = appDao.getAllProfiles()
    val userRoles: Flow<List<UserRole>> = appDao.getAllUserRoles()
    val dailyKpis: Flow<List<HostKpisDaily>> = appDao.getDailyKpis()
    val bookings: Flow<List<Booking>> = appDao.getBookings()
    val coupons: Flow<List<Coupon>> = appDao.getCoupons()

    fun getListingById(id: Int): Flow<Listing?> = appDao.getListingById(id)
    fun getLeadNotes(leadId: Int): Flow<List<LeadNote>> = appDao.getLeadNotes(leadId)
    fun getLeadEvents(leadId: Int): Flow<List<LeadEvent>> = appDao.getLeadEvents(leadId)
    fun getMessages(convId: String): Flow<List<Message>> = appDao.getAllMessages(convId)
    fun getRoomsForListing(listingId: Int): Flow<List<StayRoom>> = appDao.getRoomsForListing(listingId)
    fun getWishlist(userId: Int): Flow<List<Wishlist>> = appDao.getWishlists(userId)
    fun getVerifications(hostId: Int): Flow<List<VendorVerification>> = appDao.getVerificationsByHost(hostId)
    fun getBadges(hostId: Int): Flow<List<VendorBadge>> = appDao.getBadgesByHost(hostId)
    fun getStaffMembers(hostId: Int): Flow<List<StaffMember>> = appDao.getStaffMembers(hostId)

    suspend fun insertListing(listing: Listing): Long = withContext(Dispatchers.IO) {
        appDao.insertListing(listing)
    }

    suspend fun deleteListing(id: Int) = withContext(Dispatchers.IO) {
        appDao.deleteListingById(id)
    }

    suspend fun insertLead(lead: Lead) = withContext(Dispatchers.IO) {
        appDao.insertLead(lead)
    }

    suspend fun updateLead(lead: Lead) = withContext(Dispatchers.IO) {
        appDao.updateLead(lead)
    }

    suspend fun insertLeadNote(note: LeadNote) = withContext(Dispatchers.IO) {
        appDao.insertLeadNote(note)
    }

    suspend fun insertLeadEvent(event: LeadEvent) = withContext(Dispatchers.IO) {
        appDao.insertLeadEvent(event)
    }

    suspend fun insertReview(review: Review) = withContext(Dispatchers.IO) {
        appDao.insertReview(review)
    }

    suspend fun updateReview(review: Review) = withContext(Dispatchers.IO) {
        appDao.updateReview(review)
    }

    suspend fun insertMessage(message: Message) = withContext(Dispatchers.IO) {
        appDao.insertMessage(message)
        appDao.updateConversationMessage(message.conversationId, message.text, message.timestamp)
    }

    suspend fun addConversation(conversation: Conversation) = withContext(Dispatchers.IO) {
        appDao.insertConversation(conversation)
    }

    suspend fun addNotification(notification: Notification) = withContext(Dispatchers.IO) {
        appDao.insertNotification(notification)
    }

    suspend fun toggleWishlist(userId: Int, listingId: Int) = withContext(Dispatchers.IO) {
        val existing = appDao.getWishlists(userId).firstOrNull()?.find { it.listingId == listingId }
        if (existing != null) {
            appDao.deleteWishlist(userId, listingId)
        } else {
            appDao.insertWishlist(Wishlist(userId = userId, listingId = listingId, addedAt = "2026-06-18"))
        }
    }

    suspend fun insertOnboardingStep(step: HostOnboarding) = withContext(Dispatchers.IO) {
        appDao.insertHostOnboarding(step)
    }

    suspend fun insertRoom(room: StayRoom) = withContext(Dispatchers.IO) {
        appDao.insertRoom(room)
    }

    suspend fun insertBooking(booking: Booking) = withContext(Dispatchers.IO) {
        appDao.insertBooking(booking)
    }

    suspend fun insertVerification(v: VendorVerification) = withContext(Dispatchers.IO) {
        appDao.insertVerification(v)
    }

    suspend fun insertCoupon(coupon: Coupon) = withContext(Dispatchers.IO) {
        appDao.insertCoupon(coupon)
    }

    suspend fun seedIfNeeded() = withContext(Dispatchers.IO) {
        val checkSettings = appDao.getAppSettings().firstOrNull()
        if (checkSettings == null) {
            // Seed `app_settings` (1 row)
            appDao.insertAppSettings(
                AppSettings(
                    platformName = "StayHub",
                    commissionRate = 8.5,
                    monetizationEnabled = true,
                    maintenanceMode = false
                )
            )

            // Seed `cities` (1 row)
            appDao.insertCity(
                City(
                    name = "Pune",
                    state = "Maharashtra",
                    country = "India",
                    active = true
                )
            )

            // Seed `host_onboarding` (1 row)
            appDao.insertHostOnboarding(
                HostOnboarding(
                    hostId = 101,
                    currentStep = 3,
                    completed = false,
                    completedAt = ""
                )
            )

            // Seed `login_events` (2 rows)
            appDao.insertLoginEvent(
                LoginEvent(
                    userId = 101,
                    ipAddress = "192.168.1.12",
                    timestamp = "2026-06-18 08:12 AM",
                    deviceType = "Mobile App"
                )
            )
            appDao.insertLoginEvent(
                LoginEvent(
                    userId = 101,
                    ipAddress = "10.0.2.16",
                    timestamp = "2026-06-17 11:34 PM",
                    deviceType = "Web Dashboard"
                )
            )

            // Seed `profiles` (3 rows)
            appDao.insertProfile(
                Profile(
                    name = "Ananya Iyer",
                    email = "ananya.iyer@puneuniv.edu",
                    avatarUrl = "ananya",
                    role = "Tenant",
                    rating = 4.8,
                    phone = "+91 91234 56789",
                    joiningDate = "2025-08-20"
                )
            )
            appDao.insertProfile(
                Profile(
                    name = "Premium Elite Host",
                    email = "support@eliteaccommodation.com",
                    avatarUrl = "elite",
                    role = "Vendor",
                    rating = 4.9,
                    phone = "+91 99988 77766",
                    joiningDate = "2023-01-15"
                )
            )
            appDao.insertProfile(
                Profile(
                    name = "StayHub Operations",
                    email = "ops@stayhub.co",
                    avatarUrl = "admin",
                    role = "Admin",
                    rating = 5.0,
                    phone = "+91 80011 22334",
                    joiningDate = "2024-05-10"
                )
            )

            // Seed `user_roles` (4 rows)
            appDao.insertUserRole(
                UserRole(
                    roleName = "Tenant",
                    description = "Student looking for premium rooms, customized food services, and laundry care."
                )
            )
            appDao.insertUserRole(
                UserRole(
                    roleName = "Vendor",
                    description = "Verified business operators managing pg, hostels, catering systems, and books."
                )
            )
            appDao.insertUserRole(
                UserRole(
                    roleName = "StaffMember",
                    description = "Auxiliary service runners - delivery drivers, dry-cleaning handlers, and wardens."
                )
            )
            appDao.insertUserRole(
                UserRole(
                    roleName = "Administrator",
                    description = "Platform managers overseeing city coverage, listing dispute resolution, and coupon margins."
                )
            )

            // Custom seed listings
            val listing1Id = appDao.insertListing(
                Listing(
                    ownerId = 101,
                    title = "StayHub Premium Boys Hostel",
                    description = "Modern student living situated 500m from main Pune University. Extremely secure, high speed 500Mbps WiFi, luxury modular lounge, spacious cafeteria, biweekly dry cleaning, and certified organic meals. Dedicated live warden and CCTV tracking.",
                    categoryId = "accommodation",
                    subcategoryId = "boys_hostel",
                    serviceTypeId = "",
                    price = 7500.0,
                    rating = 4.8,
                    reviewCount = 24,
                    verifiedBadge = true,
                    availabilityStatus = "3 beds left",
                    distance = "0.5 km from Campus",
                    thumbUrl = "hostel_boys",
                    address = "Lane 3, University Road, Gokhale Nagar, Pune",
                    tags = "WiFi, AC, Power Backup, Security Guard, Gym Included",
                    extraConfigJson = """{"amenities":["WiFi","AC","Food","CCTV","Security Guard","Housekeeping","Study Area"],"occupancyRate":92,"waitlistSupport":true}"""
                )
            ).toInt()

            val listing2Id = appDao.insertListing(
                Listing(
                    ownerId = 101,
                    title = "Grand Oak Co-Ed Premium Suites",
                    description = "Airbnb state-of-the-art co-ed premium hostel suites with modern design, glass decor, personal balcony, work stations, customized ambient room controllers, and biometric locker locks. Features dry cleaning & shared library rooms.",
                    categoryId = "accommodation",
                    subcategoryId = "coed_hostel",
                    serviceTypeId = "",
                    price = 12500.0,
                    rating = 4.9,
                    reviewCount = 38,
                    verifiedBadge = true,
                    availabilityStatus = "Immediate Entry",
                    distance = "1.1 km from Campus",
                    thumbUrl = "hostel_coed",
                    address = "Plot 58, Model Colony, Shivaji Nagar, Pune",
                    tags = "WiFi, AC, Private Balcony, Smart Lock, Food, Kitchen Attached",
                    extraConfigJson = """{"amenities":["WiFi","AC","Food","Power Backup","Security Guard","Laundry","Lift","Parking"],"occupancyRate":85,"waitlistSupport":true}"""
                )
            ).toInt()

            val listing3Id = appDao.insertListing(
                Listing(
                    ownerId = 101,
                    title = "Mom's Delicacy Homestyle Daily Tiffin",
                    description = "Delicious, nutrient-dense home-cooked meals tailored for college students who miss home. Prepared in high-grade sanitary kitchens with pure RO water and organic farm-fresh greens. Delivered directly hot to your room.",
                    categoryId = "food",
                    subcategoryId = "tiffin",
                    serviceTypeId = "tiffin",
                    price = 2400.0,
                    rating = 4.7,
                    reviewCount = 142,
                    verifiedBadge = true,
                    availabilityStatus = "Slots Open",
                    distance = "Free Delivery Area",
                    thumbUrl = "tiffin",
                    address = "22B, Shanti Kunj Apartments, Senapati Bapat Road, Pune",
                    tags = "Pure Veg, Jain Option, No Preservatives, Daily Hot Delivery",
                    extraConfigJson = """{"foodType":"Veg","mealPlans":["Daily","Weekly","Monthly"],"menu":"Dal Tadka, Paneer Bhurji, Mix Veg, 4 Butter Roti, Basmati Steam Rice, Pickle"}"""
                )
            ).toInt()

            val listing4Id = appDao.insertListing(
                Listing(
                    ownerId = 101,
                    title = "CleanCloud Wash & Fold Quick Care",
                    description = "Professional garment care operation with multi-enzyme stain extraction, soft-water fabric conditioning, detailed ironing, steam pressing, and sanitization. Includes quick same-day campus pickup & doorstep delivery.",
                    categoryId = "laundry",
                    subcategoryId = "wash_iron",
                    serviceTypeId = "laundry",
                    price = 45.0,
                    rating = 4.9,
                    reviewCount = 57,
                    verifiedBadge = true,
                    availabilityStatus = "48h Standard",
                    distance = "Pickup available",
                    thumbUrl = "laundry",
                    address = "Shop 12, Campus Galleria Market, Pune",
                    tags = "Express Delivery, Stain Removal, Eco-friendly, Fabric Protection",
                    extraConfigJson = """{"pricingModels":["Per KG","Per Item"],"turnaroundTime":"48 hours","expressService":true,"garments":["Shirt","T-Shirt","Jeans","Blanket","Shoes"]}"""
                )
            ).toInt()

            val listing5Id = appDao.insertListing(
                Listing(
                    ownerId = 101,
                    title = "MindSpace Modern Silent Library & Study Hall",
                    description = "Ergonomic study desks with integrated personal charging ports, luxury reading light controllers, noise-canceling silent design, soft ambient acoustic partitioning, ultra-speed WiFi router hubs, and a complimentary locker system.",
                    categoryId = "study",
                    subcategoryId = "library",
                    serviceTypeId = "library",
                    price = 800.0,
                    rating = 4.6,
                    reviewCount = 19,
                    verifiedBadge = true,
                    availabilityStatus = "8 cabins vacant",
                    distance = "0.3 km from Campus",
                    thumbUrl = "library",
                    address = "First Floor, Synergy Arcade, Fergusson College Road, Pune",
                    tags = "AC, Quiet Zones, Personal Cabins, Dual Charging Ports, Lockers",
                    extraConfigJson = """{"amenities":["Silent Zone","AC","WiFi","Charging Ports","Locker","Personal Cabin"],"timings":"24/7 Access"}"""
                )
            ).toInt()

            // Pre-seed associated Rooms for hostel
            appDao.insertRoom(StayRoom(listingId = listing1Id, roomType = "Single Room", totalBeds = 4, availableBeds = 2, monthlyPrice = 7500.0))
            appDao.insertRoom(StayRoom(listingId = listing1Id, roomType = "Double Sharing", totalBeds = 8, availableBeds = 1, monthlyPrice = 5200.0))
            appDao.insertRoom(StayRoom(listingId = listing2Id, roomType = "Private Room Suite", totalBeds = 2, availableBeds = 1, monthlyPrice = 12500.0))

            // Pre-seed some CRM Leads!
            val lead1Id = appDao.insertLead(
                Lead(
                    listingId = listing1Id,
                    tenantName = "Rohan Verma",
                    tenantPhone = "+91 98844 55331",
                    status = "Visit Scheduled",
                    lastUpdated = "2026-06-18 09:20",
                    score = 85,
                    source = "Direct Chat"
                )
            ).toInt()

            val lead2Id = appDao.insertLead(
                Lead(
                    listingId = listing1Id,
                    tenantName = "Aarav Patel",
                    tenantPhone = "+91 81223 34455",
                    status = "Negotiating",
                    lastUpdated = "2026-06-18 10:15",
                    score = 90,
                    source = "WhatsApp Lead Link"
                )
            ).toInt()

            val lead3Id = appDao.insertLead(
                Lead(
                    listingId = listing3Id,
                    tenantName = "Meera Nair",
                    tenantPhone = "+91 75660 11223",
                    status = "New",
                    lastUpdated = "2026-06-18 12:44",
                    score = 75,
                    source = "Tiffin Plan Selector"
                )
            ).toInt()

            val lead4Id = appDao.insertLead(
                Lead(
                    listingId = listing4Id,
                    tenantName = "Divya Saxena",
                    tenantPhone = "+91 95544 33221",
                    status = "Converted",
                    lastUpdated = "2026-06-17 18:22",
                    score = 100,
                    source = "Bulk Laundry Quote"
                )
            ).toInt()

            // Seed lead notes and events
            appDao.insertLeadNote(LeadNote(leadId = lead1Id, noteText = "Prefers a double sharing room facing the garden side. Coming for visual tour tomorrow at noon with father.", authorName = "Manager Rohan", timestamp = "2026-06-18 09:25"))
            appDao.insertLeadEvent(LeadEvent(leadId = lead1Id, eventType = "VisitScheduled", timestamp = "2026-06-18 09:25", details = "Warden has blocked Room B-102 bed 2 for visual inspection"))

            appDao.insertLeadNote(LeadNote(leadId = lead2Id, noteText = "Requested dynamic coupon discount for group booking of 3 friends.", authorName = "Owner Elite", timestamp = "2026-06-18 10:18"))
            appDao.insertLeadEvent(LeadEvent(leadId = lead2Id, eventType = "Negotiating", timestamp = "2026-06-18 10:18", details = "Sent 10% coupon promo 'STAYGROUP10'"))

            // Seed Daily KPIs for premium vendor dashboard metrics (for beautiful analytics charts)
            appDao.insertDailyKpi(HostKpisDaily(hostId = 101, dateStr = "2026-06-14", revenue = 14500.0, bookingsCount = 2, leadsCount = 5, conversionRate = 40.0))
            appDao.insertDailyKpi(HostKpisDaily(hostId = 101, dateStr = "2026-06-15", revenue = 18200.0, bookingsCount = 3, leadsCount = 8, conversionRate = 37.5))
            appDao.insertDailyKpi(HostKpisDaily(hostId = 101, dateStr = "2026-06-16", revenue = 21000.0, bookingsCount = 2, leadsCount = 6, conversionRate = 33.3))
            appDao.insertDailyKpi(HostKpisDaily(hostId = 101, dateStr = "2026-06-17", revenue = 28600.0, bookingsCount = 4, leadsCount = 10, conversionRate = 40.0))
            appDao.insertDailyKpi(HostKpisDaily(hostId = 101, dateStr = "2026-06-18", revenue = 34500.0, bookingsCount = 5, leadsCount = 12, conversionRate = 41.6))

            // Seed Reviews for sentiment analyzer summaries
            appDao.insertReview(Review(listingId = listing1Id, reviewerName = "Samir Joshi", reviewerAvatar = "samir", rating = 5.0, comment = "Excellent room hygiene, robust water supply, and truly high speed internet. Best PGS hostel near Pune Campus so far!", replyText = "Thank you Samir! We strive to make StayHub feel like family.", sentiment = "Positive", timestamp = "1 day ago", containsFlags = false))
            appDao.insertReview(Review(listingId = listing3Id, reviewerName = "Kriti Sen", reviewerAvatar = "kriti", rating = 4.0, comment = "The food tastes exactly like home, very non-spicy and healthy. Delivery was 10 mins late on Wednesday though.", replyText = "Apologies Kriti, rainy day traffic delayed laundry and catering deliveries. We will keep checking real-time tracking route guidelines.", sentiment = "Mixed", timestamp = "3 days ago", containsFlags = false))
            appDao.insertReview(Review(listingId = listing4Id, reviewerName = "Vipul Roy", reviewerAvatar = "vipul", rating = 5.0, comment = "Best laundry and express dry clean. They took out deep red wine grease stains from my formal college uniform perfectly. 100% recommended.", replyText = "Glad our multi-enzyme stain extraction technology worked for you!", sentiment = "Positive", timestamp = "4 mins ago", containsFlags = false))

            // Seed Conversations and premium WhatsApp-style messages
            val convId = "conv_rohan"
            appDao.insertConversation(
                Conversation(
                    idString = convId,
                    participantName = "Rohan Verma",
                    participantAvatar = "rohan",
                    lastMessageText = "Thank you, can you tell me if single room pricing includes power backup and live laundry amenities?",
                    lastMessageTime = "09:20 AM"
                )
            )

            appDao.insertMessage(Message(conversationId = convId, senderId = 1, text = "Hey, is this StayHub support? I wanted to check availability for StayHub Boys Hostel.", timestamp = "09:10 AM", type = "Text"))
            appDao.insertMessage(Message(conversationId = convId, senderId = 101, text = "Hello Rohan! Glad you reached out. Yes, we currently have 3 beds left standard in our Single Rooms and 2 bedrooms vacant.", timestamp = "09:12 AM", type = "Text"))
            appDao.insertMessage(Message(conversationId = convId, senderId = 1, text = "Is there dry cleaning and AC available in Room B-102?", timestamp = "09:14 AM", type = "Text"))
            appDao.insertMessage(Message(conversationId = convId, senderId = 101, text = "Yes, Room B-102 has high-end split air-conditioning and our laundry services are fully included with 1-day turnaround standard.", timestamp = "09:16 AM", type = "Text"))
            appDao.insertMessage(Message(conversationId = convId, senderId = 101, text = "Here is the Room layout visual map for reference", timestamp = "09:17 AM", type = "Image", mediaUrl = "room_plan"))
            appDao.insertMessage(Message(conversationId = convId, senderId = 1, text = "Thank you, can you tell me if single room pricing includes power backup and live laundry amenities?", timestamp = "09:20 AM", type = "Text"))

            // Seed Some Notifications for Host Alert Bells
            appDao.insertNotification(Notification(title = "New Hot Lead Alert", body = "Meera Nair showed interest in 'Mom's Delicacy Tiffin' daily plan", timestamp = "6 mins ago", isRead = false, type = "NewLead"))
            appDao.insertNotification(Notification(title = "Payment Securely Received", body = "Received ₹12,500 for Booking #HB-9102 from Co-Ed Suites", timestamp = "2 hours ago", isRead = false, type = "PaymentReceived"))
            appDao.insertNotification(Notification(title = "Warden Application Verified", body = "Your business proof KYC has been verified. Green trusted partner badge granted.", timestamp = "1 day ago", isRead = true, type = "NewReview"))

            // Seed Verification documents (KYC badges)
            appDao.insertVerification(VendorVerification(hostId = 101, documentType = "Aadhaar Card", documentNumber = "XXXX-XXXX-8910", status = "Approved", submittedAt = "2026-06-15"))
            appDao.insertVerification(VendorVerification(hostId = 101, documentType = "Business GSTR GSTIN", documentNumber = "GST-27AADYP210", status = "Approved", submittedAt = "2026-06-16"))

            // Seed Coupons
            appDao.insertCoupon(Coupon(code = "WELCOMECAMPUS", discountAmount = 500.0, type = "Flat", maxUses = 100, active = true))
            appDao.insertCoupon(Coupon(code = "STUDENTMEAL20", discountAmount = 20.0, type = "Percentage", maxUses = 250, active = true))

        }
    }
}
