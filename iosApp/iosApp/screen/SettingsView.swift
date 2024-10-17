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
    @State private var areNotificationsActive = true
    @State private var showingDelays = false
    @State private var delayNotification = "10"

    var body: some View {
        VStack {
            VStack(alignment: .leading) {
                Text("Settings")
                    .font(.title)
                    .padding(
                        EdgeInsets(
                            top: 0, leading: 20, bottom: 0, trailing: 0))
                List {
                    Section {
                        HStack{
                            Text("Language")
                            Spacer()
                            Image(systemName: "chevron.right")
                        }.onTapGesture {
                            Task {
                                navigator.navigate(to: RouteSwift.language)
                            }
                        }
                    } header: {
                        Text("Accesibility")
                    }
                    Section {
                        Toggle("Notifications", isOn: $areNotificationsActive)
                        HStack {
                            let delay: String = delayNotification + " min"
                            Text("Time to notify")
                            Spacer()
                            Text(delay).font(.callout)
                        }.onTapGesture {
                            showingDelays.toggle()
                        }
                    } header: {
                        Text("Notifications")
                    }
                    Section {
                        HStack {
                            Text("Third licenses")
                            Spacer()
                            Image(systemName: "chevron.right")
                        }.onTapGesture {
                            Task {
                                navigator.navigate(to: RouteSwift.thirdLicense)
                            }
                        }
                        HStack {
                            Text("App licenses")
                            Spacer()
                            Image(systemName: "chevron.right")
                        }.onTapGesture {
                            Task {
                                navigator.navigate(to: RouteSwift.appLicense)
                            }
                        }
                    } header: {
                        Text("Licenses")
                    }
                }.listStyle(.inset)
            }
            Spacer()
            VStack(alignment: .center) {
                var message1: AttributedString {
                    var result = AttributedString("Desarrollado por ")
                    result.font = .caption
                    result.foregroundColor = .black
                    return result
                }
                
                var message2: AttributedString {
                    var result = AttributedString("Roberto García")
                    result.link = URL(string: "https://www.linkedin.com/in/rgr92")
                    result.font = .caption
                    result.foregroundColor = Color("AccentColor")
                    return result
                }
                var message3: AttributedString {
                    var result = AttributedString(" código disponible en ")
                    result.font = .caption
                    result.foregroundColor = .black
                    return result
                }
                var message4: AttributedString {
                    var result = AttributedString("Github")
                    result.link = URL(string: "https://github.com/brickbit/fosdem_app")
                    result.font = .caption
                    result.foregroundColor = Color("AccentColor")
                    return result
                }
                Text(message1+message2+message3+message4).multilineTextAlignment(.center).frame(width: 300).padding()
            }
        }.sheet(isPresented: $showingDelays) {
            VStack(alignment: .leading) {
                Text("Notify me")
                    .font(.title)
                    .padding(EdgeInsets(top: 40, leading: 20, bottom: 0, trailing: 0))
                List {
                    Text("10 min before").onTapGesture {
                        delayNotification = "10"
                        showingDelays.toggle()
                    }
                    Text("20 min before").onTapGesture {
                        delayNotification = "20"
                        showingDelays.toggle()
                    }
                    Text("30 min before").onTapGesture {
                        delayNotification = "30"
                        showingDelays.toggle()
                    }
                    Text("40 min before").onTapGesture {
                        delayNotification = "40"
                        showingDelays.toggle()
                    }
                    Text("50 min before").onTapGesture {
                        delayNotification = "50"
                        showingDelays.toggle()
                    }
                }.listStyle(.inset)
            }
            .presentationDetents([.medium, .large])
        }
    }
}

#Preview {
    SettingsView()
}
