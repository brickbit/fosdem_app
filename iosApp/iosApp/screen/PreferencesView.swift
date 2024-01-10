//
//  PreferencesView.swift
//  iosApp
//
//  Created by Roberto García Romero on 9/1/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PreferencesView: View {
    @ObservedObject var viewModel: IOSPreferencesViewModel

    @EnvironmentObject var navigator: Navigator

    init() {
        self.viewModel = IOSPreferencesViewModel()
    }
    
    var body: some View {
        VStack {
            Text("Preferences")
            Button("Next") {
                Task {
                    navigator.navigate(to: .main)
                }
            }
        }
    }
}

extension PreferencesView {
    @MainActor class IOSPreferencesViewModel: ObservableObject {
        private let viewModel: PreferencesViewModel
                
        //@Published var state: SplashStateSwift = SplashStateSwift.initialized
        
        private var handle: DisposableHandle?

        init() {
            self.viewModel = PreferencesViewModel()
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

extension TalkView {
    @MainActor class IOSTalkViewModel: ObservableObject {
        private let viewModel: TalkViewModel
                
        //@Published var state: SplashStateSwift = SplashStateSwift.initialized
        
        private var handle: DisposableHandle?

        init() {
            self.viewModel = TalkViewModel()
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
    PreferencesView()
}
