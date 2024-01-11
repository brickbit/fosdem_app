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
                            OnBoardingView()
                        case .preferences:
                            PreferencesView()
                        case .main:
                            MainView()
                        case .talk:
                            TalkView()
                        case .speaker:
                            SpeakerView()
                        }
                    }
            }
            .environmentObject(navigator)
		}
	}
}
