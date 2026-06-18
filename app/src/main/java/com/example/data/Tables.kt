package com.example.data

import androidx.room.*

@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val platformName: String,
    val commissionRate: Double,
    val monetizationEnabled: Boolean,
    val maintenanceMode: Boolean
)

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val listingId: Int,
    val tenantId: Int,
    val checkIn: String,
    val checkOut: String,
    val amountPaid: Double,
    val status: String, // Pending, Confirmed, Cancelled
    val bookingDate: String
)

@Entity(tableName = "call_sessions")
data class CallSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hostId: Int,
    val tenantId: Int,
    val status: String, // Missed, Completed
    val durationSeconds: Int,
    val timestamp: String
)

@Entity(tableName = "cities")
data class City(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val state: String,
    val country: String,
    val active: Boolean
)

@Entity(tableName = "conversations")
data class Conversation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idString: String,
    val participantName: String,
    val participantAvatar: String,
    val lastMessageText: String,
    val lastMessageTime: String
)

@Entity(tableName = "coupons")
data class Coupon(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val code: String,
    val discountAmount: Double,
    val type: String, // Percentage, Flat
    val maxUses: Int,
    val active: Boolean
)

@Entity(tableName = "delivery_tracking")
data class DeliveryTracking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Int,
    val status: String, // Dispatched, Out for Delivery, Delivered
    val courierName: String,
    val courierPhone: String,
    val trackingCode: String
)

@Entity(tableName = "host_applications")
data class HostApplication(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val businessName: String,
    val businessType: String,
    val status: String, // Pending, Rejected, Approved
    val appliedAt: String
)

@Entity(tableName = "host_kpis_daily")
data class HostKpisDaily(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hostId: Int,
    val dateStr: String,
    val revenue: Double,
    val bookingsCount: Int,
    val leadsCount: Int,
    val conversionRate: Double
)

@Entity(tableName = "host_onboarding")
data class HostOnboarding(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hostId: Int,
    val currentStep: Int,
    val completed: Boolean,
    val completedAt: String
)

@Entity(tableName = "host_policy_acceptances")
data class HostPolicyAcceptance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hostId: Int,
    val policyVersion: String,
    val acceptedAt: String
)

@Entity(tableName = "host_services")
data class HostService(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hostId: Int,
    val serviceName: String,
    val pricingModel: String, // Per KG, Hourly, Fixed
    val basePrice: Double,
    val description: String
)

@Entity(tableName = "lead_events")
data class LeadEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val leadId: Int,
    val eventType: String, // Contacted, StatusChanged, NoteAdded
    val timestamp: String,
    val details: String
)

@Entity(tableName = "lead_notes")
data class LeadNote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val leadId: Int,
    val noteText: String,
    val authorName: String,
    val timestamp: String
)

@Entity(tableName = "leads")
data class Lead(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val listingId: Int,
    val tenantName: String,
    val tenantPhone: String,
    val status: String, // New, Contacted, Interested, Visit Scheduled, Negotiating, Converted, Lost
    val lastUpdated: String,
    val score: Int,
    val source: String
)

@Entity(tableName = "ledger_entries")
data class LedgerEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hostId: Int,
    val amount: Double,
    val type: String, // Credit, Debit
    val description: String,
    val timestamp: String
)

@Entity(tableName = "listing_events")
data class ListingEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val listingId: Int,
    val eventType: String, // Viewed, Favorite, Shared
    val timestamp: String
)

@Entity(tableName = "listing_promotions")
data class ListingPromotion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val listingId: Int,
    val promoType: String, // Featured, Sponsored, PremiumHero
    val budget: Double,
    val spent: Double,
    val status: String // Active, Paused, BudgetExceeded
)

@Entity(tableName = "listings")
data class Listing(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ownerId: Int,
    val title: String,
    val description: String,
    val categoryId: String,       // category code: e.g. "accommodation", "food", "laundry", "study", "books"
    val subcategoryId: String,    // subcategory code: e.g. "girls_hostel", "tiffin_daily", "wash_iron", "library", "used_books"
    val serviceTypeId: String,    // service type if applicable
    val price: Double,
    val rating: Double,
    val reviewCount: Int,
    val verifiedBadge: Boolean,
    val availabilityStatus: String, // e.g. "4 beds left", "Live: 85% full", "Immediate Startup", "Slots open"
    val distance: String,          // e.g. "1.2 km away", "Within campus"
    val thumbUrl: String,          // decorative image alias or empty
    val address: String,
    val tags: String,              // comma-separated tags
    val extraConfigJson: String    // generic JSON/string storing specific values (e.g. amenities, meal packages, wash items)
)

@Entity(tableName = "login_events")
data class LoginEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val ipAddress: String,
    val timestamp: String,
    val deviceType: String
)

@Entity(tableName = "marketplace_categories")
data class MarketplaceCategory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val code: String,
    val name: String,
    val iconName: String
)

@Entity(tableName = "marketplace_service_types")
data class MarketplaceServiceType(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: String,
    val code: String,
    val name: String
)

@Entity(tableName = "marketplace_subcategories")
data class MarketplaceSubcategory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val serviceTypeId: String,
    val code: String,
    val name: String
)

@Entity(tableName = "message_offers")
data class MessageOffer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val messageId: Int,
    val price: Double,
    val description: String,
    val status: String // Draft, Sent, Accepted, Declined
)

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val conversationId: String,
    val senderId: Int,
    val text: String,
    val type: String, // Text, Image, Audio, Template, Offer
    val mediaUrl: String = "",
    val voiceDuration: Int = 0, // seconds
    val timestamp: String
)

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val body: String,
    val timestamp: String,
    val isRead: Boolean,
    val type: String, // NewLead, NewBooking, NewOrder, PaymentReceived, NewReview
    val metadataJson: String = ""
)

@Entity(tableName = "pickup_zones")
data class PickupZone(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val serviceId: Int,
    val zoneName: String,
    val radiusKm: Double,
    val active: Boolean
)

@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val avatarUrl: String,
    val role: String, // Admin, Vendor, Tenant
    val rating: Double,
    val phone: String,
    val joiningDate: String
)

@Entity(tableName = "quick_replies")
data class QuickReply(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val shortcut: String,
    val messageText: String
)

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val listingId: Int,
    val reviewerName: String,
    val reviewerAvatar: String,
    val rating: Double,
    val comment: String,
    val replyText: String,
    val sentiment: String, // Positive, Centered, Mixed, Negative
    val timestamp: String,
    val containsFlags: Boolean
)

@Entity(tableName = "room_beds")
data class RoomBed(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val roomId: Int,
    val bedNumber: String,
    val occupantName: String = "",
    val status: String // Vacant, Occupied
)

@Entity(tableName = "rooms")
data class StayRoom(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val listingId: Int,
    val roomType: String, // Single, Double Sharing, Triple Sharing, Dormitory
    val totalBeds: Int,
    val availableBeds: Int,
    val monthlyPrice: Double
)

@Entity(tableName = "saved_searches")
data class SavedSearch(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val queryText: String,
    val filtersJson: String,
    val timestamp: String
)

@Entity(tableName = "staff_members")
data class StaffMember(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hostId: Int,
    val name: String,
    val role: String, // Delivery, Laundry Handler, Cook, Warden
    val permissions: String, // All, Limited
    val active: Boolean
)

@Entity(tableName = "tenants")
data class Tenant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val hostId: Int,
    val leaseStart: String,
    val leaseEnd: String,
    val rentAmount: Double,
    val status: String // Active, Terminated
)

@Entity(tableName = "user_roles")
data class UserRole(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val roleName: String,
    val description: String
)

@Entity(tableName = "vendor_badges")
data class VendorBadge(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hostId: Int,
    val badgeType: String, // Verified, Trusted Vendor, Premium Vendor
    val awardedAt: String
)

@Entity(tableName = "vendor_verifications")
data class VendorVerification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hostId: Int,
    val documentType: String, // Aadhaar, PAN, Business Proof
    val documentNumber: String,
    val status: String, // Pending, Approved, Rejected
    val submittedAt: String
)

@Entity(tableName = "wishlists")
data class Wishlist(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val listingId: Int,
    val addedAt: String
)
