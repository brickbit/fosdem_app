import SwiftUI
import shared
import Resolver

struct ContentView: View {
    @StateObject var viewModel: SplashViewModelSwift = Resolver.resolve()

    init() {
        viewModel.wrapper.initializeSplash()
    }
	let greet = Greeting().greet()

	var body: some View {
        Text(Greeting().greet())
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}

class SplashViewModelSwift: ObservableObject {
    var wrapper: SplashViewModel = SplashViewModel()
    @Published private(set) var state: SplashState = SplashState.Init()

}
