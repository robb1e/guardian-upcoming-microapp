package com.gu.integration

import dispatch._
import dispatch.thread.ThreadSafeHttpClient
import org.apache.http.params.HttpParams
import org.apache.http.conn.params.ConnRouteParams

trait ConsoleLog {
  def log(message: String) {
    println("[test] %s" format message)
  }
}

object IntegrationHttpClient extends ConsoleLog {

  def get(uri: String): String = apply(url(uri))

  def get(uri: String, headers: Map[String, String] = Map(), params: Traversable[(String, String)] = Map()): String = apply(url(uri) <:< headers <<? params)

  def post(uri: String) = apply(url(uri) << Map())

  def post(uri: String, params: Traversable[(String, String)]) = apply(url(uri) << params)

  def postWithBody(uri: String, body: String, params: Traversable[(String, String)] = Map(), headers: Map[String, String] = Map()) = apply(url(uri) <:< headers << body <<? params)

  def put(uri: String, params: Traversable[(String, String)] = Map()) = apply(url(uri) <<< params)

  def post(uri: String, params: Traversable[(String, String)] = Map(), headers: Map[String, String] = Map()) = apply(url(uri) <:< headers << params)

  def delete(uri: String) = apply(url(uri).DELETE)

  def apply(request: Request) = {
    log("Retriving resource %s %s%s" format (request.method, request.host, request.path))
    ThreadSafeHttp(request as_str)
  }

  object ThreadSafeHttp extends Http with thread.Safety {
    // This is there to NOT go through the proxy, as that won't recognise localhost addresses
    override def make_client = new ThreadSafeHttpClient(new Http.CurrentCredentials(None), maxConnections = 50, maxConnectionsPerRoute = 50) {
      override protected def configureProxy(params: HttpParams) = {
        ConnRouteParams.setDefaultProxy(params, null)
        params
      }
    }
  }
}
