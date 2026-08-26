package user.profile.messageDTO

data class ResponseMessageDTO (
    val status: String,
    val code: Int,
    val message: String,
    val data: Any? = null
)