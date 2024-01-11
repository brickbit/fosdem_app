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
        TabView {
            OnboardItemView(imageName: "logo", title: "First page", description: "Something", actionEnabled: false, action: {})
            OnboardItemView(imageName: "logo", title: "Second page", description: "Something",actionEnabled: false, action: {})
            OnboardItemView(imageName: "logo", title: "Third page", description: "Something", actionEnabled: true, action: {
                Task {
                    navigator.navigate(to: .preferences)
                }
            })
        }
        .tabViewStyle(.page(indexDisplayMode: .always))
        .indexViewStyle(.page(backgroundDisplayMode: .always))
        /*VStack {
            Text("OnBoarding")
            Button("Next") {
                Task {
                    navigator.navigate(to: .preferences)
                }
            }
        }*/
    }
}

struct OnboardItemView: View {
    let imageName: String
    let title: String
    let description: String
    let actionEnabled: Bool
    let action: () -> ()
    
    var body: some View {
        VStack(spacing: 20) {
            Image(imageName)
                .resizable()
                .scaledToFit()
                .frame(width: 100, height: 100)
                .foregroundColor(.teal)
            
            Text(title)
                .font(.title).bold()
            
            Text(description)
                .multilineTextAlignment(.center)
                .foregroundColor(.secondary)
            if(actionEnabled) {
                Button("Next") {
                    action()
                }
            }
        }
        .padding(.horizontal, 40)
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
