import SwiftUI
import shared
@main
struct iOSApp: App {
    
    @ObservedObject var navigator = Navigator()
    
    init() {
        HelperKt.doInitKoin()
    }
    
	var body: some Scene {
		WindowGroup {
            NavigationStack(path: $navigator.navPath) {
                SplashView()
                    .navigationDestination(for: RouteSwift.self) { destination in
                        switch destination {
                        case .splash:
                            SplashView()
                        case .onBoarding:
                            SplashView()//OnBoardingView()
                        case .preferences:
                            SplashView()//PreferencesView()
                        case .main:
                            MainView()
                        case .talk:
                            SplashView()//TalkView()
                        case .speaker:
                            SplashView()//SpeakerView()
                        case .language:
                            LanguageView()
                        case .thirdLicense:
                            ThirdLicenseView()
                        case .appLicense:
                            AppLicenseView()
                        }
                
                    }
            }
            .environmentObject(navigator)
		}
	}
}
