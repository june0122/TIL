# 네트워크 연결 상태 관리

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object TwitchApiModule {
    private const val BASE_URL = "https://api.twitch.tv/helix/"

    @Singleton
    @Provides
    fun provideTwitchService(
        @ApplicationContext context: Context,
        preferencesRepository: PreferencesRepositoryImpl,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): TwitchService {
        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(TwitchAuthInterceptor(preferencesRepository))
            .addInterceptor(NetworkConnectionInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TwitchService::class.java)
    }

    class TwitchAuthInterceptor @Inject constructor(
        private val preferencesRepository: PreferencesRepositoryImpl
    ) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val twitchAccessToken: String

            runBlocking {
                twitchAccessToken = preferencesRepository.flowTwitchAccessTokens().first()
            }

            val newRequest = request().newBuilder()
                .addHeader("Authorization", "Bearer $twitchAccessToken")
                .addHeader("Client-Id", BuildConfig.TWITCH_CLIENT_ID)
                .build()

            proceed(newRequest)
        }
    }

    class NetworkConnectionInterceptor @Inject constructor(
        @ApplicationContext private val context: Context
    ) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            return if (isOnline().not()) {
                throw NetworkOfflineException()
            } else {
                chain.proceed(chain.request())
            }
        }

        private fun isOnline(): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            val connection = connectivityManager.getNetworkCapabilities(network)

            return connection != null &&
                    (connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        }

        class NetworkOfflineException : IOException() {
            override val message: String
                get() = "인터넷 연결이 끊겼습니다. WIFI나 데이터 연결을 확인해주세요."
        }
    }
}
```