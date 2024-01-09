//
//  OnBoardingView.swift
//  iosApp
//
//  Created by Roberto García Romero on 9/1/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct OnBoardingView: View {
    @ObservedObject var viewModel: IOSOnBoardingViewModel

    @EnvironmentObject var navigator: Navigator

    init() {
        self.viewModel = IOSOnBoardingViewModel()
    }
    
    var body: some View {
        VStack {
            Text("OnBoarding")
            Button("Next") {
                Task {
                    navigator.navigate(to: .preferences)
                }
            }
        }
    }
}

extension OnBoardingView {
    @MainActor class IOSOnBoardingViewModel: ObservableObject {
        private let viewModel: OnBoardingViewModel
                
        //@Published var state: SplashStateSwift = SplashStateSwift.initialized
        
        private var handle: DisposableHandle?

        init() {
            self.viewModel = OnBoardingViewModel()
            //self.viewModel.initializeSplash()
        }
        
        // Observes to state changes
        func startObserving() {
            /*handle = viewModel.state.subscribe(onCollect: { state in
                if let state = state {
                    self.state = SplashStateSwift(state) ?? .initialized
                }
            })*/
        }
        
        // Removes the listener
        func dispose() {
            handle?.dispose()
        }
    }
}

#Preview {
    OnBoardingView()
}
