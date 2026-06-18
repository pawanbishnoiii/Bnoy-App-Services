package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM listings ORDER BY id DESC")
    fun getAllListings(): Flow<List<Listing>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: Listing): Long

    @Query("DELETE FROM listings WHERE id = :id")
    suspend fun deleteListingById(id: Int)

    @Query("SELECT * FROM listings WHERE id = :id LIMIT 1")
    fun getListingById(id: Int): Flow<Listing?>

    // Leads / CRM
    @Query("SELECT * FROM leads ORDER BY id DESC")
    fun getAllLeads(): Flow<List<Lead>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLead(lead: Lead): Long

    @Update
    suspend fun updateLead(lead: Lead)

    @Query("SELECT * FROM lead_notes WHERE leadId = :leadId ORDER BY id DESC")
    fun getLeadNotes(leadId: Int): Flow<List<LeadNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeadNote(note: LeadNote)

    @Query("SELECT * FROM lead_events WHERE leadId = :leadId ORDER BY id DESC")
    fun getLeadEvents(leadId: Int): Flow<List<LeadEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeadEvent(event: LeadEvent)

    // Reviews
    @Query("SELECT * FROM reviews ORDER BY id DESC")
    fun getAllReviews(): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review)

    @Update
    suspend fun updateReview(review: Review)

    // Messages / Conversations
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY id ASC")
    fun getAllMessages(conversationId: String): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)

    @Query("SELECT * FROM conversations ORDER BY lastMessageTime DESC")
    fun getConversations(): Flow<List<Conversation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: Conversation)

    @Query("UPDATE conversations SET lastMessageText = :text, lastMessageTime = :time WHERE idString = :idString")
    suspend fun updateConversationMessage(idString: String, text: String, time: String)

    // Notifications
    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun getAllNotifications(): Flow<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notification)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markNotificationAsRead(id: Int)

    // Profiles
    @Query("SELECT * FROM profiles ORDER BY id ASC")
    fun getAllProfiles(): Flow<List<Profile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: Profile)

    // User Roles
    @Query("SELECT * FROM user_roles ORDER BY id ASC")
    fun getAllUserRoles(): Flow<List<UserRole>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserRole(role: UserRole)

    // App Settings
    @Query("SELECT * FROM app_settings LIMIT 1")
    fun getAppSettings(): Flow<AppSettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppSettings(settings: AppSettings)

    // Cities
    @Query("SELECT * FROM cities ORDER BY id ASC")
    fun getCities(): Flow<List<City>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: City)

    // Host Onboarding
    @Query("SELECT * FROM host_onboarding ORDER BY id ASC")
    fun getHostOnboardings(): Flow<List<HostOnboarding>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHostOnboarding(onboarding: HostOnboarding)

    // Login Events
    @Query("SELECT * FROM login_events ORDER BY id DESC")
    fun getLoginEvents(): Flow<List<LoginEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoginEvent(event: LoginEvent)

    // Daily KPIs
    @Query("SELECT * FROM host_kpis_daily ORDER BY id DESC")
    fun getDailyKpis(): Flow<List<HostKpisDaily>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyKpi(kpi: HostKpisDaily)

    // Vendor Verifications
    @Query("SELECT * FROM vendor_verifications WHERE hostId = :hostId ORDER BY id DESC")
    fun getVerificationsByHost(hostId: Int): Flow<List<VendorVerification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerification(verification: VendorVerification)

    // Vendor Badges
    @Query("SELECT * FROM vendor_badges WHERE hostId = :hostId ORDER BY id DESC")
    fun getBadgesByHost(hostId: Int): Flow<List<VendorBadge>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadge(badge: VendorBadge)

    // Bookings
    @Query("SELECT * FROM bookings ORDER BY id DESC")
    fun getBookings(): Flow<List<Booking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking)

    // Coupons
    @Query("SELECT * FROM coupons ORDER BY id ASC")
    fun getCoupons(): Flow<List<Coupon>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoupon(coupon: Coupon)

    // Staff
    @Query("SELECT * FROM staff_members WHERE hostId = :hostId ORDER BY id ASC")
    fun getStaffMembers(hostId: Int): Flow<List<StaffMember>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStaffMember(staff: StaffMember)

    // Rooms
    @Query("SELECT * FROM rooms WHERE listingId = :listingId")
    fun getRoomsForListing(listingId: Int): Flow<List<StayRoom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: StayRoom)

    // Saved Searches
    @Query("SELECT * FROM saved_searches ORDER BY id DESC")
    fun getSavedSearches(): Flow<List<SavedSearch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedSearch(search: SavedSearch)

    // Wishlists
    @Query("SELECT * FROM wishlists WHERE userId = :userId")
    fun getWishlists(userId: Int): Flow<List<Wishlist>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlist(wishlist: Wishlist)

    @Query("DELETE FROM wishlists WHERE userId = :userId AND listingId = :listingId")
    suspend fun deleteWishlist(userId: Int, listingId: Int)
}
