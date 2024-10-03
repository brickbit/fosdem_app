//
//  Extension.swift
//  iosApp
//
//  Created by Roberto Garcia Romero on 3/10/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation

extension String {
    subscript(i: Int) -> String {
        return String(self[index(startIndex, offsetBy: i)])
    }
}
