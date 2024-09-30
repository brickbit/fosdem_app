//
//  SettingsView.swift
//  iosApp
//
//  Created by Roberto Garcia Romero on 29/9/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct SettingsView: View {
    @EnvironmentObject var navigator: Navigator

    var body: some View {
        VStack(alignment: .leading) {
            Text("Settings")
                .font(.title)
                .padding(
                    EdgeInsets(
                        top: 0, leading: 20, bottom: 0, trailing: 0))
            List {
                Text("Language").onTapGesture {
                    Task {
                        navigator.navigate(to: RouteSwift.talk)
                    }
                }
                Text("Notifications")
                Text("Time to notify")
                Text("Third licenses").onTapGesture {
                    Task {
                        navigator.navigate(to: RouteSwift.thirdLicense)
                    }
                }
                Text("App licenses").onTapGesture {
                    Task {
                        navigator.navigate(to: RouteSwift.appLicense)
                    }
                }
            }.listStyle(.inset)
                
        }
    }
}

#Preview {
    SettingsView()
}
