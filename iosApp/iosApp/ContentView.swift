import SwiftUI
import shared
import Resolver

struct ContentView: View {
    var body: some View {
        SplashScreen()
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}

struct SplashScreen: View {
    @ObservedObject var viewModel: IOSSplashViewModel
    init() {
        self.viewModel = IOSSplashViewModel()
    }
    var body: some View {
        VStack {
            splashContent()
        }
        .onAppear {
            viewModel.startObserving()
        }
        .onDisappear {
            viewModel.dispose()
        }
    }
    
    func splashContent() -> AnyView {
        switch viewModel.state {
        case .initialized: return AnyView(Text("Initialized"))
        case .finished: return AnyView(Text("Finished"))
        }
    }
    
}

extension SplashScreen {
    @MainActor class IOSSplashViewModel: ObservableObject {
        private let viewModel: SplashViewModel
                
        @Published var state: SplashStateSwift = SplashStateSwift.initialized
        
        private var handle: DisposableHandle?

        init() {
            self.viewModel = SplashViewModel()
        }
        
        // Observes to state changes
        func startObserving() {
            handle = viewModel.state.subscribe(onCollect: { state in
                if let state = state {
                    self.state = SplashStateSwift(state) ?? .initialized
                }
            })
        }
        
        // Removes the listener
        func dispose() {
            handle?.dispose()
        }
    }
}

enum SplashStateSwift {
    case initialized
    case finished
}

extension SplashStateSwift {
    init?(_ value: SplashState) {
        switch value {
        case is SplashState.Init:
            self = .initialized
        case is SplashState.Finished:
            self = .finished
        default:
            return nil
        }
        self = SplashStateSwift.finished
    }
}
