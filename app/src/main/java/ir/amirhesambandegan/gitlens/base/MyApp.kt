package ir.amirhesambandegan.gitlens.base

import android.app.Application
import ir.amirhesambandegan.easify_network.createKtorClient
import ir.amirhesambandegan.gitlens.repository.HomeRepository
import ir.amirhesambandegan.gitlens.viewModel.HomeViewModel
import ir.amirhesambandegan.gitlens.viewModel.UserDetailViewModel
import ir.amirhesambandegan.gitlens.viewModel.RepoDetailViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class MyApp : Application() {
    private val modules = module {
        single {
            createKtorClient(
                "https://api.github.com",
                requestTimeoutMillis = 15000,
                socketTimeoutMillis = 15000,
                connectTimeoutMillis = 15000
            )
        }

        factory { HomeRepository(get()) }
        viewModel { HomeViewModel(get()) }
        viewModel { UserDetailViewModel(get()) }
        viewModel { RepoDetailViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            androidLogger(Level.DEBUG)
            modules(modules)
        }
    }
}