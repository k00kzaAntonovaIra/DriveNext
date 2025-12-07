package com.example.drivenext_antonova.data

import kotlinx.coroutines.delay

/**
 * Repository for car data operations
 */
object CarRepository {

    private val mockCars = listOf(
        CarData(
            id = "1",
            model = "S 500 Sedan",
            brand = "Mercedes-Benz",
            pricePerDay = 2500,
            gearbox = GearboxType.AUTOMATIC,
            fuel = FuelType.GASOLINE,
            year = 2023,
            mileage = 15000,
            seats = 5,
            transmission = TransmissionType.REAR_WHEEL
        ),
        CarData(
            id = "2",
            model = "Camry",
            brand = "Toyota",
            pricePerDay = 1800,
            gearbox = GearboxType.AUTOMATIC,
            fuel = FuelType.GASOLINE,
            year = 2022,
            mileage = 25000,
            seats = 5,
            transmission = TransmissionType.FRONT_WHEEL
        ),
        CarData(
            id = "3",
            model = "Model S",
            brand = "Tesla",
            pricePerDay = 5000,
            gearbox = GearboxType.AUTOMATIC,
            fuel = FuelType.ELECTRIC,
            year = 2023,
            mileage = 8000,
            seats = 5,
            transmission = TransmissionType.ALL_WHEEL
        ),
        CarData(
            id = "4",
            model = "X5",
            brand = "BMW",
            pricePerDay = 3200,
            gearbox = GearboxType.AUTOMATIC,
            fuel = FuelType.DIESEL,
            year = 2022,
            mileage = 18000,
            seats = 7,
            transmission = TransmissionType.ALL_WHEEL
        ),
        CarData(
            id = "5",
            model = "A4",
            brand = "Audi",
            pricePerDay = 2200,
            gearbox = GearboxType.MANUAL,
            fuel = FuelType.GASOLINE,
            year = 2021,
            mileage = 35000,
            seats = 5,
            transmission = TransmissionType.FRONT_WHEEL
        )
    )

    /**
     * Fetches all available cars
     */
    suspend fun fetchAllCars(): Result<List<CarData>> {
        return try {
            delay(1200) // Simulate network delay
            Result.Success(mockCars)
        } catch (e: Exception) {
            Result.Error("Не удалось загрузить данные. Попробуйте снова.", e)
        }
    }

    /**
     * Searches cars by brand
     */
    suspend fun searchByBrand(brandQuery: String): Result<List<CarData>> {
        return try {
            delay(800) // Simulate network delay
            val filtered = mockCars.filter { 
                it.brand.contains(brandQuery, ignoreCase = true) ||
                it.model.contains(brandQuery, ignoreCase = true)
            }
            Result.Success(filtered)
        } catch (e: Exception) {
            Result.Error("Не удалось выполнить поиск. Попробуйте снова.", e)
        }
    }

    /**
     * Gets car by ID
     */
    suspend fun getCarById(id: String): Result<CarData?> {
        return try {
            delay(500) // Simulate network delay
            val car = mockCars.find { it.id == id }
            Result.Success(car)
        } catch (e: Exception) {
            Result.Error("Не удалось загрузить данные автомобиля.", e)
        }
    }

    /**
     * Searches cars by price range
     */
    suspend fun searchByPriceRange(minPrice: Int, maxPrice: Int): Result<List<CarData>> {
        return try {
            delay(600) // Simulate network delay
            val filtered = mockCars.filter { 
                it.pricePerDay in minPrice..maxPrice 
            }
            Result.Success(filtered)
        } catch (e: Exception) {
            Result.Error("Не удалось выполнить поиск по цене.", e)
        }
    }
}