//
//  MainView.swift
//  iosApp
//
//  Created by Roberto García Romero on 9/1/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct MainView: View {
   
    
    var body: some View {
        TabView {
            HomeView().tabItem {
                Label("Home", systemImage: "house.fill")
            }
            ScheduleListView().tabItem {
                Label("Schedules", systemImage: "calendar")
            }
            .toolbarBackground(.visible, for: .tabBar)
            .toolbarBackground(Color.black, for: .tabBar)
            VideoListView().tabItem {
                Label("Videos", systemImage: "play.rectangle.fill")
            }
            .toolbarBackground(.visible, for: .tabBar)
            .toolbarBackground(Color.black, for: .tabBar)
            SettingsView().tabItem {
                Label("Settings", systemImage: "wrench")
            }
        }
        .tint(Color("AccentColor"))
        .navigationBarBackButtonHidden()
            
    }
}


#Preview {
    MainView()
}
