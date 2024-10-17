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
    @State var degreesRotating = 0.0

    var body: some View {
        VStack {
            Spacer()
            ZStack {
                Image("LogoInside")
                    .resizable()
                    .frame(width: 200.0, height: 200.0)
                Image("LogoOutside")
                    .resizable()
                    .frame(width: 200.0, height: 200.0)
                    .rotationEffect(.degrees(360))
                    .rotationEffect(.degrees(degreesRotating))
                    .onAppear {
                        withAnimation(.linear(duration: 1)
                            .speed(0.3).repeatForever(autoreverses: false)) {
                                degreesRotating = 360.0
                            }
                      }

            }
            Spacer()
            VStack {
                Text("FOSDEM").font(.title).padding()
                Text("Cargando datos...").font(.headline)
                //.font(.custom("Signika-Bold",size: 48))
            }.padding()
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
