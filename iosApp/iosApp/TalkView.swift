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

#Preview {
    TalkView()
}
