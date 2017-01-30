//
//  SpeedInfraction+CoreDataProperties.swift
//  Speedster
//
//  Created by Hasnain Bilgrami on 11/2/16.
//  Copyright © 2016 Hasnain Bilgrami. All rights reserved.
//

import Foundation
import CoreData


extension SpeedInfraction {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<SpeedInfraction> {
        return NSFetchRequest<SpeedInfraction>(entityName: "SpeedInfraction");
    }

    @NSManaged public var latitdue: String?
    @NSManaged public var longitude: String?
    @NSManaged public var date: NSDate?
    @NSManaged public var currentSpeed: String?
    @NSManaged public var speeLimit: String?
    @NSManaged public var infractionTime: Double

}
