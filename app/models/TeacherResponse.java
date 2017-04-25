/*
 * Copyright (C) 2011 mPower Health
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package models;

import play.Logger;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Data Model - Definition of survey form data.
 */
@Entity
public class TeacherResponse extends Model {

	@ManyToOne
	public Data data;

	@ManyToOne
	public User user;

	@ManyToOne
	public SchoolInformation schools;

	public String isSolved;
	public String reason;
	public String comment;


}
