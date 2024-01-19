//
//  TalkView.swift
//  iosApp
//
//  Created by Roberto García Romero on 9/1/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct TalkView: View {
    @ObservedObject var viewModel: IOSTalkViewModel

    @EnvironmentObject var navigator: Navigator

    init() {
        self.viewModel = IOSTalkViewModel()
    }
    
    var body: some View {
        VStack {
            Text("Talk")
            Button("Next") {
                Task {
                    navigator.navigate(to: .speaker)
                }
            }
        }
    }
}

extension TalkView {
    @MainActor class IOSTalkViewModel: ObservableObject {
        private let viewModel: TalkViewModel
                
        //@Published var state: SplashStateSwift = SplashStateSwift.initialized
        
        private var handle: DisposableHandle?

        init() {
            self.viewModel =  GetViewModels().getTalkViewModel()
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
    TalkView()
}
