//
//  HomeView.swift
//  iosApp
//
//  Created by Roberto Garcia Romero on 29/9/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct HomeView: View {
    var body: some View {
        VStack {
            HStack {
                Text("Right now")
                Spacer()
            }
            HStack {
                Text("Favourite talks")
                Spacer()
                Text("see all")
            }
            HStack {
                Text("Speakers")
                Spacer()
                Text("see all")
            }
            HStack {
                Text("Stands")
                Spacer()
            }
        }.padding()
    }
}

#Preview {
    HomeView()
}
