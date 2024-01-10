import SwiftUI
import shared
import Resolver

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
        case .initialized: return AnyView(Text("Initialized"))
        case .finished:
            Task{
                navigator.navigate(to: .onBoarding)
            }
            return AnyView(EmptyView())
        }
    }
    
}

extension SplashView {
    @MainActor class IOSSplashViewModel: ObservableObject {
        private let viewModel: SplashViewModel
                
        @Published var state: SplashStateSwift = SplashStateSwift.initialized
        
        private var handle: DisposableHandle?

        init() {
            self.viewModel = SplashViewModel(getSchedule: GetScheduleDataUseCase(repository: ScheduleRepositoryImpl()))
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

