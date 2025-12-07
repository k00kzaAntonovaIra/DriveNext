package com.example.drivenext_antonova.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents car data with all necessary information for car rental
 */
@Parcelize
data class CarData(
    val id: String,
    val model: String,
    val brand: String,
    val pricePerDay: Int,
    val gearbox: GearboxType,
    val fuel: FuelType,
    val imageResId: Int? = null,
    val imageUrl: String? = null,
    val year: Int? = null,
    val mileage: Int? = null,
    val seats: Int? = null,
    val transmission: TransmissionType? = null
) : Parcelable {

    /**
     * Returns formatted price string
     */
    val formattedPrice: String get() = "${pricePerDay}₽/день"

    /**
     * Returns full car name (brand + model)
     */
    val fullName: String get() = "$brand $model"

    /**
     * Returns true if car has image
     */
    val hasImage: Boolean get() = imageResId != null || !imageUrl.isNullOrBlank()
}

/**
 * Represents gearbox types
 */
@Parcelize
enum class GearboxType(val displayName: String) : Parcelable {
    AUTOMATIC("Автомат"),
    MANUAL("Механика"),
    SEMI_AUTOMATIC("Полуавтомат")
}

/**
 * Represents fuel types
 */
@Parcelize
enum class FuelType(val displayName: String) : Parcelable {
    GASOLINE("Бензин"),
    DIESEL("Дизель"),
    ELECTRIC("Электро"),
    HYBRID("Гибрид"),
    GAS("Газ")
}

/**
 * Represents transmission types
 */
@Parcelize
enum class TransmissionType(val displayName: String) : Parcelable {
    FRONT_WHEEL("Передний привод"),
    REAR_WHEEL("Задний привод"),
    ALL_WHEEL("Полный привод")
}