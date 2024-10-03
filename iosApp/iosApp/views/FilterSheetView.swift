//
//  FilterSheetView.swift
//  iosApp
//
//  Created by Roberto Garcia Romero on 1/10/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct FilterSheetView: View {
    
    let roomList: [String]
    let trackList: [String]
    let dayList: [String]
    let hours: [String]

    @State var room: String
    @State var track: String
    @State var day: String
    @State var selectedHours: [String] = []

    var onFilterClicked: (String, String, String, [String]) -> Void
    
    var body: some View {
        VStack(alignment: .leading) {
            Text("Filter by")
                .font(.title)
            Text("Day")
            Picker("Select the day", selection: $day) {
                ForEach(dayList, id: \.self) {
                    Text($0)
                }
            }
            .pickerStyle(.menu)

            Text("Track")
            Picker("Select the day", selection: $track) {
                ForEach(trackList, id: \.self) {
                    Text($0)
                }
            }
            .pickerStyle(.menu)
            Text("Room")
            Picker("Select the room", selection: $room) {
                ForEach(roomList, id: \.self) {
                    Text($0)
                }
            }
            .pickerStyle(.menu)
            Text("Hours")
            ScrollView(.horizontal) {
                LazyHStack {
                    ForEach(hours, id: \.self) { hour in
                        ChipView(
                            name: hour,
                            isSelected: false,
                            onSelectChip: { hour in
                                selectedHours.append(hour)
                            },
                            onDeselectChip: { hour in
                                selectedHours.removeAll(where: { item in
                                    item == hour
                                })
                            }
                        )
                    }
                }
            }
            Button("Filter") {
                onFilterClicked(room, track, day, selectedHours)
            }
            .frame(maxWidth: .infinity, maxHeight: 50)
            .background(
                mainGradient
            )
            .foregroundColor(.white)
            .cornerRadius(25)

        }.padding(EdgeInsets(top: 40, leading: 20, bottom: 20, trailing: 20))
    }
}


#Preview {
    FilterSheetView(roomList: [], trackList: [], dayList: [], hours: [], room: "", track: "", day: "") {_,_,_,_ in }
}
