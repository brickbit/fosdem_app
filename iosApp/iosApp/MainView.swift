//
//  MainView.swift
//  iosApp
//
//  Created by Roberto García Romero on 9/1/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct MainView: View {
    @ObservedObject var viewModel: IOSMainViewModel

    @EnvironmentObject var navigator: Navigator

    init() {
        self.viewModel = IOSMainViewModel()
    }
    
    var body: some View {
        VStack {
            Text("Main")
            Button("Next") {
                Task {
                    navigator.navigate(to: .talk)
                }
            }
        }
    }
}

extension MainView {
    @MainActor class IOSMainViewModel: ObservableObject {
        private let viewModel: MainViewModel
                
        //@Published var state: SplashStateSwift = SplashStateSwift.initialized
        
        private var handle: DisposableHandle?

        init() {
            self.viewModel = MainViewModel()
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
    MainView()
}
