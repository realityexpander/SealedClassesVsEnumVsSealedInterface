package com.realityexpander.sealedclassesvsenumvssealedinterface


// Sealed class with no constructors
sealed class HttpErrorSealed1 {
    object Unauthorized : HttpErrorSealed1()
    object NotFound : HttpErrorSealed1()

    fun getErrorMessage(): String {
        return when (this) {
            Unauthorized -> "Unauthorized"
            NotFound -> "Not Found"
            //else -> "Unknown" // not needed if all cases are covered

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
            // else -> "Unknown: unknown code" // not needed if all cases are covered

        }
    }
    }

// Pro: Enums use less memory and can be checked at compile time.
// Con: All parameters for all enums must be included in the constructor with defaults for unused parameters.
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
            // else -> "Unknown: unknown code" // not needed if all cases are covered
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
            // else -> "Unknown" // not needed if all cases are covered

        }
    }
}

// Sealed interface - complex & hierarchical
// Pros: Sealed interfaces can be used to create a hierarchy of classes.
// Pros: Allows for more complex hierarchies than sealed classes.
// Pros: Using a hierarchy allows for easier to read code (ie: Unauthorized.WithMessage)
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

        object Simple : Extra(200)

        data class WithData(
            override val data: String
        ) : Extra(201, data = data)

        data class WithDataAndMessage(
            override val message: String,
            override val data: String,
        ) : Extra(202, message, data)
    }

    sealed class Unauthorized(
        override val code: Int,
        override val message: String? = null
    ) : HttpErrorSealedInterface2 {

        object Simple
            : Unauthorized(401)

        data class WithMessage(override val message: String)
            : Unauthorized(402, message)
    }

    sealed class NotFound(
        override val code: Int,
        override val message: String? = null
    ) : HttpErrorSealedInterface2 {

        object Simple
            : NotFound(404, "Not Found")
    }

    // Note: The cases below may not be exhaustive. No compiler warning is given.
    // Note: The cases below are arranged from MOST specific to most general. (hierarchical)
    //       If the specific cases are not caught first, the more general cases will be caught last.
    fun getErrorMessage(): String {
        return when (this) {
            is Unauthorized.Simple -> "Unauthorized.Simple: $code"
            is Unauthorized.WithMessage -> "Unauthorized.WithMessage: $code, $message"
            is Unauthorized -> "Unauthorized: $code, $message"

            is NotFound.Simple -> "NotFound.Simple: $code, $message"
            is NotFound -> "NotFound: $code"

            is Impossible -> "Impossible: $code, $message" // should never be reached.

            is Default -> "Default: $code, $message"

            is Extra.Simple -> "Extra.Simple: $code"
            is Extra.WithData -> "Extra.WithData: $code, $data"
            is Extra.WithDataAndMessage -> "Extra.WithDataAndMessage: $code, $message, $data"
            is Extra -> "Extra: $code, $message, $data"

            else -> "Unknown: unknown code" // Needed because all no warning is given for unimplemented cases.
        }
    }
}



































