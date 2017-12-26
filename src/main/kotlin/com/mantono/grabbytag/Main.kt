package com.mantono.grabbytag

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

fun main(args: Array<String>)
{
	val user = args[0]
	val tags = args.sliceArray(1 .. args.lastIndex).toList()
	val limitRepos = 200
	val limitTopics = 10

	val apiToken: String? = System.getenv("GITHUB_API_TOKEN")
	val authHeader: Pair<String, String> = apiToken?.let { Pair("Authorization", "bearer $it") } ?: Pair("", "")

	println(apiToken)
	val query = """
	query
	{
  		organization(login: "$user")
		{
    		repositories(first: $limitRepos, orderBy: {field: CREATED_AT, direction: DESC})
			{
      			nodes
	  			{
        			name
        			repositoryTopics(first: $limitTopics)
					{
          				nodes
		  				{
            				topic
							{
              					id
              					name
            				}
          				}
        			}
      			}
    		}
  		}
	}"""

	//ObjectMapper().readTree(query)

	val result = khttp.post(
			url = "https://api.github.com/graphql",
			data = query,
			timeout = 30.0,
			headers = mapOf(authHeader)
	)

	println(result.text)
}