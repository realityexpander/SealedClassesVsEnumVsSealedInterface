package com.example.sealedclassesvsenumvssealedinterface


// Sealed class with no constructors
sealed class HttpErrorSealed1 {
    object Unauthorized : HttpErrorSealed1()
    object NotFound : HttpErrorSealed1()

    fun getErrorMessage(): String {
        return when (this) {
            Unauthorized -> "Unauthorized"
            NotFound -> "Not Found"
            else -> "Unknown"

        }
    }
}

// Sealed class with constructors
// note: Parameters common to all subclasses are included in the primary constructor.
sealed class HttpErrorSealed2(val code: Int) {
    object Unauthorized : HttpErrorSealed2(401)
    data class UnauthorizedWithMessage(val message: String) : HttpErrorSealed2(401)
    object NotFound : HttpErrorSealed2(404)

    fun getErrorMessage(): String {
        return when (this) {
            Unauthorized -> "Unauthorized: $code"
            is UnauthorizedWithMessage -> "Unauthorized: $code, $message"
            NotFound -> "Not Found: $code"
            else -> "Unknown: unknown code"

        }
    }
    }

// Pro: Enums use less memory and can be checked at compile time.
// Con: All parameters for all enums must be included in the constructor.
// Con: Enum parameters cannot be passed in at runtime. (but they can be modified)
// Pro: `when` expressions show warning if not exhaustive.
enum class HttpErrorEnum(val code: Int, var message: String? = null) {
    Unauthorized(401),
    UnauthorizedWithMessage(401, "UnauthorizedWithMessage"),
    NotFound(404);  // <-- must terminate with semicolon

    fun getErrorMessage(): String {
        return when (this) {
            Unauthorized -> "Unauthorized $code"
            UnauthorizedWithMessage -> "UnauthorizedWithMessage: $code, $message"
            NotFound -> "Not Found $code"
            else -> "Unknown: unknown code"
        }
    }
}

// Sealed interface - simple
// Pros: Sealed interfaces can be used to create a hierarchy of classes that can be checked at compile time.
// Cons: Sealed interfaces cannot have constructors.
// Pros: Sealed interfaces can be simpler to use than sealed classes.
// Cons: `when` expressions are no longer exhaustive and show no warning for unimplemented cases.
sealed interface HttpErrorSealedInterface1 {
    object Unauthorized : HttpErrorSealedInterface1
    data class UnauthorizedWithMessage(val message: String) : HttpErrorSealedInterface1
    object NotFound : HttpErrorSealedInterface1

    fun getErrorMessage(): String {
        return when (this) {
            Unauthorized -> "Unauthorized"
            is UnauthorizedWithMessage -> "Unauthorized ${this.message}"
            NotFound -> "Not Found"
            else -> "Unknown"

        }
    }
}

// Sealed interface - complex
// Pros: Sealed interfaces can be used to create a hierarchy of classes that can be checked at compile time.
// Pros: Allows for more complex hierarchies than sealed classes.
// Cons: primary constructor cannot be used.
// Cons: Must use "inner classes" to create the types.
// Cons: `when` expressions are no longer exhaustive and show no warning for unimplemented cases.
sealed interface HttpErrorSealedInterface2 {
    val code: Int
    val message: String?

    // Note: Can instantiate objects (but can't use a constructor.)
    object Default: HttpErrorSealedInterface2 {
        override val code: Int = 100
        override val message: String = "default"
    }

    // Note: *CANNOT* instantiate a sealed class without it having its own inner data classes or objects.
    sealed class Impossible(
        override val code: Int,
        override val message: String? = null
    ): HttpErrorSealedInterface2 {
        // IMPORTANT: SEALED CLASSES REQUIRE AT LEAST ONE INNER CLASS OR OBJECT IN ORDER TO BE INSTANTIATED.
    }

    sealed class Extra(
        override val code: Int,
        override val message: String? = null,
        open val data: String? = null
    ): HttpErrorSealedInterface2 {
        object ExtraSimple : Extra(200)

        data class ExtraWithData(
            override val data: String
        ) : Extra(201, data = data)

        data class ExtraWithDataAndMessage(
            override val message: String,
            override val data: String,
        ) : Extra(202, message, data)
    }

    sealed class UnauthorizedBase(
        override val code: Int,
        override val message: String? = null
    ) : HttpErrorSealedInterface2 {
        object Unauthorized
            : UnauthorizedBase(401)

        data class UnauthorizedWithMessage(override val message: String)
            : UnauthorizedBase(402, message)
    }

    sealed class NotFoundBase(
        override val code: Int,
        override val message: String? = null
    ) : HttpErrorSealedInterface2 {
        object NotFound
            : NotFoundBase(404, "Not Found")
    }

    // Note: The cases below are not exhaustive.
    // Note: The cases below are arranged from MOST specific to most general.
    fun getErrorMessage(): String {
        return when (this) {
            is UnauthorizedBase.Unauthorized -> "Unauthorized: $code"
            is UnauthorizedBase.UnauthorizedWithMessage -> "UnauthorizedWithMessage: $code, $message"
            is UnauthorizedBase -> "UnauthorizedBase: $code, $message"
            is NotFoundBase.NotFound -> "NotFound: $code, $message"
            is NotFoundBase -> "NotFoundBase: $code"
            is Impossible -> "Impossible: $code, $message"
            is Default -> "Default: $code, $message"
            is Extra.ExtraSimple -> "ExtraSimple: $code"
            is Extra.ExtraWithData -> "ExtraWithData: $code, $data"
            is Extra.ExtraWithDataAndMessage -> "ExtraWithDataAndMessage: $code, $message, $data"
            is Extra -> "Extra: $code, $message, $data"
            else -> "Unknown: unknown code"
        }
    }
}



































