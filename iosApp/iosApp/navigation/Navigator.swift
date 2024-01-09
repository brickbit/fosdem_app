//
//  Navigator.swift
//  iosApp
//
//  Created by Roberto García Romero on 8/1/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI

final class Navigator: ObservableObject {
    
    @Published var navPath = NavigationPath()
    
    func navigate(to route: RouteSwift) {
        navPath.append(route)
    }
    
    func navigateBack() {
        navPath.removeLast()
    }
    
    func navigateToRoot() {
        navPath.removeLast(navPath.count)
    }
}
