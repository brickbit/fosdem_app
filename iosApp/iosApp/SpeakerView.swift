//
//  SpeakerView.swift
//  iosApp
//
//  Created by Roberto García Romero on 9/1/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SpeakerView: View {
    @ObservedObject var viewModel: IOSSpeakerViewModel

    @EnvironmentObject var navigator: Navigator

    init() {
        self.viewModel = IOSSpeakerViewModel()
    }
    
    var body: some View {
        VStack {
            Text("Speaker")
        }
    }
}

extension SpeakerView {
    @MainActor class IOSSpeakerViewModel: ObservableObject {
        private let viewModel: SpeakerViewModel
                
        //@Published var state: SplashStateSwift = SplashStateSwift.initialized
        
        private var handle: DisposableHandle?

        init() {
            self.viewModel = SpeakerViewModel()
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
    SpeakerView()
}
