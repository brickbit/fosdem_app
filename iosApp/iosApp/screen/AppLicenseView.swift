//
//  AppLicenseView.swift
//  iosApp
//
//  Created by Roberto Garcia Romero on 29/9/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct AppLicenseView: View {
    var body: some View {
        VStack(alignment: .leading) {
            Text("App license")
                .font(.title)
                .padding(
                    EdgeInsets(
                        top: 0, leading: 20, bottom: 0, trailing: 0))
            CustomWebView()
        }
       
    }
}

#Preview {
    AppLicenseView()
}
