import SwiftUI
import shared

struct SplashView: View {
    @EnvironmentObject var navigator: Navigator
    @ObservedObject private var viewModel = SplashViewModelWrapper()
    
    var body: some View {
        VStack {
            SplashView()
        }.task {
            viewModel.initialize()
        }
    }
    
    func SplashView() -> AnyView {
        guard let route = viewModel.state.route else {
            return AnyView(SplashScreen())
        }
        switch RouteSwift(route) {
        case .main:
            Task {
                navigator.navigate(to: RouteSwift.main)
            }
            return AnyView(SplashScreen())
        default:
            return AnyView(SplashScreen())
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
                //.font(.custom("Signika-Bold",size: 48))
        }
    }
}


class SplashViewModelWrapper: ObservableObject {
    private let viewModel: SplashViewModel = GetViewModels().getSplashViewModel()
    
    @Published var state: SplashState = SplashState(route: nil, isError: false)
    
    
    func initialize() {
        FlowWrapper<SplashState>(stateFlow: viewModel.state).observe { states in
            self.state = states ?? SplashState(route: nil, isError: false)
        }
        viewModel.initializeSplash()
    }
    
    
}

struct SplashView_Previews: PreviewProvider {
    static var previews: some View {
        SplashView()
    }
}
