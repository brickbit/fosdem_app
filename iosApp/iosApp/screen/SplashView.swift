import SwiftUI
import shared

struct SplashView: View {
    @ObservedObject var viewModel: IOSSplashViewModel
    @EnvironmentObject var navigator: Navigator

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
        case .initialized: return AnyView(SplashScreen())
        case .finished(let route):
            Task{
                navigator.navigate(to: route)
            }
            return AnyView(EmptyView())
        case .error:
            return AnyView(Text("Error"))
        }
    }
}

struct SplashScreen: View {
    var body: some View {
        VStack {
            Image("logo")
                .resizable()
                .frame(width: 240.0, height: 240.0)
            Text("FOSDEM")
                .font(.custom("Signika-Bold",size: 48))
        }
    }
}

extension SplashView {
    @MainActor class IOSSplashViewModel: ObservableObject {
        private let viewModel: SplashViewModel
                
        @Published var state: SplashStateSwift = SplashStateSwift.initialized
        
        private var handle: DisposableHandle?

        init() {
            self.viewModel = GetViewModels().getSplashViewModel()
            self.viewModel.initializeSplash()
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

struct SplashView_Previews: PreviewProvider {
    static var previews: some View {
        SplashView()
    }
}


