package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        AppSettings::class,
        Booking::class,
        CallSession::class,
        City::class,
        Conversation::class,
        Coupon::class,
        DeliveryTracking::class,
        HostApplication::class,
        HostKpisDaily::class,
        HostOnboarding::class,
        HostPolicyAcceptance::class,
        HostService::class,
        LeadEvent::class,
        LeadNote::class,
        Lead::class,
        LedgerEntry::class,
        ListingEvent::class,
        ListingPromotion::class,
        Listing::class,
        LoginEvent::class,
        MarketplaceCategory::class,
        MarketplaceServiceType::class,
        MarketplaceSubcategory::class,
        MessageOffer::class,
        Message::class,
        Notification::class,
        PickupZone::class,
        Profile::class,
        QuickReply::class,
        Review::class,
        RoomBed::class,
        StayRoom::class,
        SavedSearch::class,
        StaffMember::class,
        Tenant::class,
        UserRole::class,
        VendorBadge::class,
        VendorVerification::class,
        Wishlist::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "stayhub_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
