//
//  VideoListView.swift
//  iosApp
//
//  Created by Roberto Garcia Romero on 29/9/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct VideoListView: View {
    var body: some View {
        VStack {
            Text("Videos")
                .font(.title)
                .padding(
                    EdgeInsets(
                        top: 0, leading: 20, bottom: 0, trailing: 0))
        }
    }
}

#Preview {
    VideoListView()
}
